package com.example.gymmanagerment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MembersByPackageActivity extends AppCompatActivity {
    ListView listView;
    ArrayAdapter<String> adapter;
    List<String> memberNames;
    List<String> memberIds;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_by_package);

        listView = findViewById(R.id.listViewMembers);
        memberNames = new ArrayList<>();
        memberIds = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, memberNames);
        listView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        String packageId = getIntent().getStringExtra("packageId");
        if (packageId != null) {
            loadMembersByPackage(packageId);
        }

        // Thiết lập sự kiện click cho ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Lấy ID từ danh sách memberIds dựa trên vị trí click
                String selectedMemberId = memberIds.get(position);

                // Chuyển sang MemberDetailActivity và truyền ID
                Intent intent = new Intent(MembersByPackageActivity.this, MemberDetailActivity.class);
                intent.putExtra("memberId", selectedMemberId);
                startActivity(intent);
            }
        });
    }

    private void loadMembersByPackage(String packageId) {
        db.collection("Members")
                .whereEqualTo("package_id", packageId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    memberNames.clear();
                    memberIds.clear(); // Xóa dữ liệu cũ
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String name = doc.getString("name");
                        String memberId = doc.getId(); // Lấy ID của document
                        if (name != null) {
                            memberNames.add(name);
                            memberIds.add(memberId); // Thêm ID vào danh sách
                        }
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}