package com.example.gymmanagerment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
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
    private List<Member> allMembers;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        recyclerView = findViewById(R.id.recyclerView);
        btnAddMember = findViewById(R.id.btnAddMember);
        searchView = findViewById(R.id.searchView);

        memberList = new ArrayList<>();
        allMembers = new ArrayList<>();
        adapter = new MemberAdapter(memberList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadMembers();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // không làm gì khi nhấn enter
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterMembers(newText);
                return true;
            }
        });

        // Thêm thành viên
        btnAddMember.setOnClickListener(v -> {
            startActivity(new Intent(MemberActivity.this, AddMemberActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMembers(); // cập nhật lại dữ liệu khi quay về từ màn hình khác
    }

    private void loadMembers() {
        db.collection("Members")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    memberList.clear();
                    allMembers.clear();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Member member = doc.toObject(Member.class);
                        member.setId(doc.getId());

                        if (member.getName() != null) {
                            memberList.add(member);
                            allMembers.add(member);
                        }
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreError", e.getMessage());
                    Toast.makeText(this, "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
                });
    }

    private void filterMembers(String query) {
        List<Member> filteredList = new ArrayList<>();
        for (Member member : allMembers) {
            if (member.getName() != null && member.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(member);
            }
        }

        memberList.clear();
        memberList.addAll(filteredList);
        adapter.notifyDataSetChanged();
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
                    loadMembers();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi xóa: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
        deleteMemberWithAttendance(memberId);
    }
    private void deleteMemberWithAttendance(String memberId) {
        db.collection("Attendance")
                .whereEqualTo("memberId", memberId)
                .get()
                .addOnSuccessListener(attSnapshots -> {
                    for (DocumentSnapshot doc : attSnapshots) {
                        doc.getReference().delete();
                    }
                });
    }


}
