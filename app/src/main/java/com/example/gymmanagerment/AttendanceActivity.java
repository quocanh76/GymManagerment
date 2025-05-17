package com.example.gymmanagerment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AttendanceActivity extends AppCompatActivity implements MemberAttendanceAdapter.OnMemberClickListener {

    private RecyclerView recyclerView;
    private FloatingActionButton fabTodayCheckin;
    private FirebaseFirestore db;
    private MemberAttendanceAdapter adapter;
    private List<Member> members = new ArrayList<>();
    private List<Member> filteredMembers = new ArrayList<>(); // Danh sách đã lọc
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        recyclerView = findViewById(R.id.recyclerViewMembers);
        fabTodayCheckin = findViewById(R.id.fabTodayCheckin);
        searchView = findViewById(R.id.searchView);
        db = FirebaseFirestore.getInstance();

        adapter = new MemberAttendanceAdapter(filteredMembers, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fabTodayCheckin.setOnClickListener(v -> checkinAllMembers());

        setupSearchView();
        loadMembersWithCheckinCounts();
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterMembers(newText);
                return true;
            }
        });
    }

    private void filterMembers(String query) {
        filteredMembers.clear();

        if (query.isEmpty()) {
            filteredMembers.addAll(members);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (Member member : members) {
                if (member.getName().toLowerCase().contains(lowerCaseQuery)) {
                    filteredMembers.add(member);
                }
            }
        }

        adapter.notifyDataSetChanged();
    }

    private void loadMembersWithCheckinCounts() {
        db.collection("Members").get().addOnSuccessListener(memberSnapshots -> {
            members.clear();
            filteredMembers.clear();

            for (DocumentSnapshot doc : memberSnapshots) {
                Member member = doc.toObject(Member.class);
                if (member != null) {
                    member.setId(doc.getId());
                    members.add(member);
                }
            }

            filteredMembers.addAll(members);

            String currentMonth = new SimpleDateFormat("MM/yyyy", Locale.getDefault()).format(new Date());
            String today = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

            db.collection("Attendance").get().addOnSuccessListener(attSnapshots -> {
                for (Member member : members) {
                    int count = 0;
                    boolean checkedInToday = false;

                    for (DocumentSnapshot att : attSnapshots) {
                        String date = att.getString("date");
                        String memberId = att.getString("memberId");

                        if (memberId != null && memberId.equals(member.getId()) && date != null) {
                            if (date.endsWith(currentMonth)) {
                                count++;
                            }
                            if (date.equals(today)) {
                                checkedInToday = true;
                            }
                        }
                    }

                    member.setMonthlyCheckinCount(count);
                    member.setCheckedInToday(checkedInToday);
                }

                adapter.notifyDataSetChanged();
            });
        });
    }

    private void checkinMember(String memberId, String memberName) {
        String today = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        db.collection("Members").document(memberId).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        String endDateStr = doc.getString("end_date");
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                            Date endDate = sdf.parse(endDateStr);
                            Date now = new Date();

                            if (endDate != null && now.after(endDate)) {
                                Toast.makeText(this, memberName + " đã hết hạn, không thể điểm danh", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (Exception e) {
                            Toast.makeText(this, "Lỗi định dạng ngày: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Tiếp tục điểm danh nếu còn hạn
                        performCheckin(memberId, memberName, today);
                    }
                });
    }


    private void updateMemberCheckinInfo(String memberId) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("lastCheckin", FieldValue.serverTimestamp());
        updates.put("checkinCount", FieldValue.increment(1));
        updates.put("checkedInToday", true);

        db.collection("Members").document(memberId)
                .update(updates)
                .addOnFailureListener(e -> Log.e("FirestoreError", e.getMessage()));
    }

    private void checkinAllMembers() {
        for (Member member : filteredMembers) { // Sử dụng filteredMembers thay vì members
            if (!member.isCheckedInToday()) {
                checkinMember(member.getId(), member.getName());
            }
        }
    }

    @Override
    public void onMemberClick(String memberId, String memberName) {
        checkinMember(memberId, memberName);
    }

    @Override
    public void onCheckinHistoryClick(String memberId) {
        Intent intent = new Intent(this, AttendanceHistoryActivity.class);
        intent.putExtra("memberId", memberId);
        startActivity(intent);
    }

    @Override
    public void onCheckinTodayClick(String memberId) {
        Member member = findMemberById(memberId);
        if (member != null) {
            checkinMember(memberId, member.getName());
        }
    }

    private Member findMemberById(String memberId) {
        for (Member member : members) {
            if (member.getId().equals(memberId)) {
                return member;
            }
        }
        return null;
    }
    private void performCheckin(String memberId, String memberName, String today) {
        db.collection("Attendance")
                .whereEqualTo("memberId", memberId)
                .whereEqualTo("date", today)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        Toast.makeText(this, memberName + " đã điểm danh hôm nay", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Map<String, Object> checkinData = new HashMap<>();
                    checkinData.put("memberId", memberId);
                    checkinData.put("date", today);
                    checkinData.put("timestamp", FieldValue.serverTimestamp());

                    db.collection("Attendance").add(checkinData)
                            .addOnSuccessListener(documentReference -> {
                                updateMemberCheckinInfo(memberId);
                                Toast.makeText(this, "Đã điểm danh cho " + memberName, Toast.LENGTH_SHORT).show();
                                loadMembersWithCheckinCounts();
                            });
                });
    }

}