package com.example.gymmanagerment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MemberActivity extends AppCompatActivity implements MemberAdapter.MemberClickListener {
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private FloatingActionButton btnAddMember;
    private MemberAdapter adapter;
    private List<Member> memberList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        // Khởi tạo views
        recyclerView = findViewById(R.id.recyclerView);
        btnAddMember = findViewById(R.id.btnAddMember);

        // Thiết lập RecyclerView
        memberList = new ArrayList<>();
        adapter = new MemberAdapter(memberList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Sự kiện thêm thành viên
        btnAddMember.setOnClickListener(v ->
                startActivity(new Intent(MemberActivity.this, AddMemberActivity.class)));

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();
        loadMembers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMembers();
    }

    private void loadMembers() {
        db.collection("Members")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    memberList.clear();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Member member = doc.toObject(Member.class);
                        member.setId(doc.getId()); // Lưu document ID
                        memberList.add(member);
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreError", e.getMessage());
                    Toast.makeText(this, "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onMemberClick(int position) {
        Member member = memberList.get(position);
        Intent intent = new Intent(this, MemberDetailActivity.class);
        intent.putExtra("memberId", member.getId());
        startActivity(intent);
    }

    @Override
    public void onEditClick(int position) {
        Member member = memberList.get(position);
        Intent intent = new Intent(this, EditMemberActivity.class);
        intent.putExtra("memberId", member.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(int position) {
        Member member = memberList.get(position);

        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa thành viên \"" + member.getName() + "\"?")
                .setPositiveButton("Xóa", (dialog, which) -> deleteMember(member.getId()))
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteMember(String memberId) {
        db.collection("Members").document(memberId)
                .delete()
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Đã xóa thành viên", Toast.LENGTH_SHORT).show();
                    loadMembers(); // Tải lại danh sách
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi xóa: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}