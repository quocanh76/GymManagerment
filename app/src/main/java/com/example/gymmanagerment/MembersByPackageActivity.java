package com.example.gymmanagerment;

import android.os.Bundle;
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
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_by_package);

        listView = findViewById(R.id.listViewMembers);
        memberNames = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, memberNames);
        listView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        String packageId = getIntent().getStringExtra("packageId");
        if (packageId != null) {
            loadMembersByPackage(packageId);
        }
    }

    private void loadMembersByPackage(String packageId) {
        db.collection("Members")
                .whereEqualTo("package_id", packageId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    memberNames.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String name = doc.getString("name");
                        if (name != null) memberNames.add(name);
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}

