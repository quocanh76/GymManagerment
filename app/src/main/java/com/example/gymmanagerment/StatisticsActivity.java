package com.example.gymmanagerment;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.*;

public class StatisticsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView tvTotalMembers, tvTotalCheckins, tvTopMembers, tvTodayCheckins, tvTopPackages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        db = FirebaseFirestore.getInstance();

        tvTotalMembers = findViewById(R.id.tvTotalMembers);
        tvTotalCheckins = findViewById(R.id.tvTotalCheckins);
        tvTopMembers = findViewById(R.id.tvTopMembers);
        tvTodayCheckins = findViewById(R.id.tvTodayCheckins);
        tvTopPackages = findViewById(R.id.tvTopPackages);


        loadStatistics();
    }

    private void loadStatistics() {
        db.collection("Members").get().addOnSuccessListener(memberSnapshots -> {
            int totalMembers = memberSnapshots.size();
            tvTotalMembers.setText(String.valueOf(totalMembers));

            Map<String, String> memberNames = new HashMap<>();
            Map<String, Integer> packageCount = new HashMap<>();

            for (DocumentSnapshot doc : memberSnapshots) {
                String name = doc.getString("name");
                String packageId = doc.getString("package_id");

                if (name != null && !name.trim().isEmpty()) {
                    memberNames.put(doc.getId(), name);
                }

                if (packageId != null && !packageId.trim().isEmpty()) {
                    packageCount.put(packageId, packageCount.getOrDefault(packageId, 0) + 1);
                }
            }

            String currentMonth = new SimpleDateFormat("MM/yyyy", Locale.getDefault()).format(new Date());
            String todayStr = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

            db.collection("Attendance").get().addOnSuccessListener(attSnapshots -> {
                int totalCheckins = 0;
                int todayCheckins = 0;
                Map<String, Integer> checkinCountMap = new HashMap<>();

                for (DocumentSnapshot doc : attSnapshots) {
                    String memberId = doc.getString("memberId");
                    String date = doc.getString("date");

                    if (date != null) {
                        if (date.endsWith(currentMonth)) {
                            totalCheckins++;
                            checkinCountMap.put(memberId, checkinCountMap.getOrDefault(memberId, 0) + 1);
                        }

                        if (date.equals(todayStr)) {
                            todayCheckins++;
                        }
                    }
                }

                tvTotalCheckins.setText(String.valueOf(totalCheckins));
                tvTodayCheckins.setText("Điểm danh hôm nay: " + todayCheckins);

                List<Map.Entry<String, Integer>> sorted = new ArrayList<>(checkinCountMap.entrySet());
                sorted.sort((a, b) -> b.getValue() - a.getValue());

                StringBuilder topMembers = new StringBuilder("Top 5 thành viên chăm chỉ nhất:\n");
                for (int i = 0; i < Math.min(5, sorted.size()); i++) {
                    String id = sorted.get(i).getKey();
                    int count = sorted.get(i).getValue();
                    topMembers.append(i + 1).append(". ").append(memberNames.get(id)).append(": ").append(count).append(" lần\n");
                }

                tvTopMembers.setText(topMembers.toString());


                List<Map.Entry<String, Integer>> sortedPackages = new ArrayList<>(packageCount.entrySet());
                sortedPackages.sort((a, b) -> b.getValue() - a.getValue());

                StringBuilder topPackages = new StringBuilder("Top gói được dùng nhiều nhất:\n");
                for (int i = 0; i < Math.min(5, sortedPackages.size()); i++) {
                    String packageId = sortedPackages.get(i).getKey();
                    int count = sortedPackages.get(i).getValue();
                    topPackages.append(i + 1).append(". Gói ").append(packageId).append(": ").append(count).append(" thành viên\n");
                }

                tvTopPackages.setText(topPackages.toString());


            });
        });
    }
}
