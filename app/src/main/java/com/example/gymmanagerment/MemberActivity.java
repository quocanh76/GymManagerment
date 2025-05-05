package com.example.gymmanagerment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.HashMap;

public class MemberActivity extends AppCompatActivity {
    FirebaseFirestore db;
    ListView listView;
    ArrayList<String> memberNames;
    ArrayList<String> memberIds; // Chứa document ID
    ArrayAdapter<String> adapter;
    FloatingActionButton btnAddMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        listView = findViewById(R.id.listView);
        btnAddMember = findViewById(R.id.btnAddMember);

        memberNames = new ArrayList<>();
        memberIds = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, memberNames);
        listView.setAdapter(adapter);

        btnAddMember.setOnClickListener(v ->
                startActivity(new Intent(MemberActivity.this, AddMemberActivity.class)));

        db = FirebaseFirestore.getInstance();
        loadMembers();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String memberId = memberIds.get(position);
            Intent intent = new Intent(MemberActivity.this, MemberDetailActivity.class);
            intent.putExtra("memberId", memberId);
            startActivity(intent);
        });


            listView.setOnItemLongClickListener((parent, view, position, id) -> {
                String memberId = memberIds.get(position);
                String memberName = memberNames.get(position);

                new AlertDialog.Builder(this)
                        .setTitle("Chọn hành động")
                        .setMessage("Bạn muốn làm gì với thành viên \"" + memberName + "\"?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            deleteMember(memberId);
                        })
                        .setNeutralButton("Thay đổi thông tin", (dialog, which) -> {
                            editMember(memberId);
                        })
                        .setNegativeButton("Hủy", null)
                        .show();

                return true;
            });


        }
    private void deleteMember(String memberId) {
        db.collection("Members").document(memberId)
                .delete()
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Đã xóa thành viên", Toast.LENGTH_SHORT).show();
                    loadMembers(); // Tải lại danh sách thành viên
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi xóa: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
    private void editMember(String memberId) {
        Intent intent = new Intent(MemberActivity.this, EditMemberActivity.class);
        intent.putExtra("memberId", memberId);
        startActivity(intent);
    }

    private void loadMembers() {
        db.collection("Members")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    memberNames.clear();
                    memberIds.clear();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String name = doc.getString("name");
                        if (name != null) {
                            memberNames.add(name);
                            memberIds.add(doc.getId()); // Lưu document ID tương ứng
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("FirestoreError", e.getMessage()));
    }


}
