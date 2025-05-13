package com.example.gymmanagerment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymmanagerment.Member;
import com.example.gymmanagerment.MemberAttendanceAdapter;
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

        // Khởi tạo các view và adapter
        recyclerView = findViewById(R.id.recyclerViewMembers);
        fabTodayCheckin = findViewById(R.id.fabTodayCheckin);
        db = FirebaseFirestore.getInstance();

        adapter = new MemberAttendanceAdapter(members, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Sự kiện click nút điểm danh tất cả
        fabTodayCheckin.setOnClickListener(v -> checkinAllMembers());

        //loadMembers();
    }

    // Hàm điểm danh cho 1 thành viên cụ thể
    private void checkinMember(String memberId, String memberName) {
        String today = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        // Kiểm tra đã điểm danh hôm nay chưa
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

                    // Thực hiện điểm danh
                    Map<String, Object> checkinData = new HashMap<>();
                    checkinData.put("memberId", memberId);
                    checkinData.put("date", today);
                    checkinData.put("timestamp", FieldValue.serverTimestamp());

                    db.collection("Attendance").add(checkinData)
                            .addOnSuccessListener(documentReference -> {
                                // Cập nhật thông tin thành viên
                                updateMemberCheckinInfo(memberId);
                                Toast.makeText(this,
                                        "Đã điểm danh cho " + memberName,
                                        Toast.LENGTH_SHORT).show();
                            });
                });
    }

    // Cập nhật thông tin điểm danh của thành viên
    private void updateMemberCheckinInfo(String memberId) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("lastCheckin", FieldValue.serverTimestamp());
        updates.put("checkinCount", FieldValue.increment(1));
        updates.put("checkedInToday", true);

        db.collection("Members").document(memberId)
                .update(updates)
                .addOnFailureListener(e -> Log.e("FirestoreError", e.getMessage()));
    }

    // Điểm danh tất cả thành viên chưa điểm danh
    private void checkinAllMembers() {
        String today = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        for (Member member : members) {
            if (!member.isCheckedInToday()) {
                checkinMember(member.getId(), member.getName());
            }
        }
    }

    @Override
    public void onMemberClick(String memberId, String memberName) {
        // Xử lý khi click vào item thành viên
        checkinMember(memberId, memberName);
    }

    @Override
    public void onCheckinHistoryClick(String memberId) {
        // Mở màn hình lịch sử điểm danh
        Intent intent = new Intent(this, AttendanceHistoryActivity.class);
        intent.putExtra("memberId", memberId);
        startActivity(intent);
    }

    @Override
    public void onCheckinTodayClick(String memberId) {
        // Xử lý khi click nút điểm danh hôm nay (nếu có nút riêng)
        // Có thể gọi cùng phương thức checkinMember()
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