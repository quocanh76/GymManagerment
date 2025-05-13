package com.example.gymmanagerment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PackagesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Package> packageList;
    PackageAdapter adapter;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packages);

        recyclerView = findViewById(R.id.recyclerViewPackages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        packageList = new ArrayList<>();
        adapter = new PackageAdapter(packageList, this);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadPackages();
    }

    private void loadPackages() {
        db.collection("Packages").get().addOnSuccessListener(queryDocumentSnapshots -> {
            packageList.clear();
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                Package pack = doc.toObject(Package.class);
                pack = new Package(doc.getId(), pack.getName(), pack.getDuration());
                packageList.add(pack);
            }
            adapter.notifyDataSetChanged();
        });
    }
}

