package com.example.gymmanagerment;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class AttendanceHistoryActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> dates;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_history);

        listView = findViewById(R.id.listviewhistory);
        dates = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dates);
        listView.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();

        String memberId = getIntent().getStringExtra("memberId");
        if (memberId != null) {
            loadAttendanceHistory(memberId);
        } else {
            Toast.makeText(this, "Không tìm thấy thành viên", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadAttendanceHistory(String memberId) {
        db.collection("Attendance")
                .whereEqualTo("memberId", memberId)
                .get()
                .addOnSuccessListener(query -> {
                    dates.clear();
                    for (QueryDocumentSnapshot doc : query) {
                        String date = doc.getString("date");
                        if (date != null) {
                            dates.add(date);
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi tải lịch sử điểm danh", Toast.LENGTH_SHORT).show();
                });
    }
}
