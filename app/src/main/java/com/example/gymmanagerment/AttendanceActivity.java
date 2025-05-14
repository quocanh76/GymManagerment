package com.example.gymmanagerment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        recyclerView = findViewById(R.id.recyclerViewMembers);
        fabTodayCheckin = findViewById(R.id.fabTodayCheckin);
        db = FirebaseFirestore.getInstance();

        adapter = new MemberAttendanceAdapter(members, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fabTodayCheckin.setOnClickListener(v -> checkinAllMembers());

        loadMembersWithCheckinCounts();
    }

    private void loadMembersWithCheckinCounts() {
        db.collection("Members").get().addOnSuccessListener(memberSnapshots -> {
            members.clear();

            for (DocumentSnapshot doc : memberSnapshots) {
                Member member = doc.toObject(Member.class);
                if (member != null) {
                    member.setId(doc.getId());
                    members.add(member);
                }
            }

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

        db.collection("Attendance")
                .whereEqualTo("memberId", memberId)
                .whereEqualTo("date", today)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        Toast.makeText(this,
                                memberName + " đã điểm danh hôm nay",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Map<String, Object> checkinData = new HashMap<>();
                    checkinData.put("memberId", memberId);
                    checkinData.put("date", today);
                    checkinData.put("timestamp", FieldValue.serverTimestamp());

                    db.collection("Attendance").add(checkinData)
                            .addOnSuccessListener(documentReference -> {
                                updateMemberCheckinInfo(memberId);
                                Toast.makeText(this,
                                        "Đã điểm danh cho " + memberName,
                                        Toast.LENGTH_SHORT).show();

                                loadMembersWithCheckinCounts();
                            });
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
        for (Member member : members) {
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
}
