package com.example.gymmanagerment;

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
        db.collection("Packages")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    packageList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String id = doc.getId();
                        String name = doc.getString("name");
                        int duration = doc.getLong("duration").intValue();
                        int price = doc.getLong("price").intValue();

                        Package pack = new Package(id, name, duration, price);
                        packageList.add(pack);
                    }

                    packageList.sort((p1, p2) -> Integer.compare(p1.getDuration(), p2.getDuration()));

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                });
    }

}
