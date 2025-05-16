package com.example.gymmanagerment;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ExpiredActivity extends AppCompatActivity {

    private ListView lvExpired, lvAboutToExpire;
    private ArrayAdapter<String> expiredAdapter, aboutToExpireAdapter;
    private ArrayList<String> expiredList, aboutToExpireList;
    private FirebaseFirestore db;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expired);

        lvExpired = findViewById(R.id.lvExpired);
        lvAboutToExpire = findViewById(R.id.lvAboutToExpire);

        expiredList = new ArrayList<>();
        aboutToExpireList = new ArrayList<>();

        expiredAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, expiredList);
        aboutToExpireAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, aboutToExpireList);

        lvExpired.setAdapter(expiredAdapter);
        lvAboutToExpire.setAdapter(aboutToExpireAdapter);

        db = FirebaseFirestore.getInstance();
        loadExpiredMembers();
    }

    private void loadExpiredMembers() {
        db.collection("Members")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    expiredList.clear();
                    aboutToExpireList.clear();

                    Date today = new Date();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(today);
                    calendar.add(Calendar.MONTH, 1);
                    Date oneMonthLater = calendar.getTime();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String name = doc.getString("name");
                        String endDateStr = doc.getString("end_date");

                        try {
                            Date endDate = sdf.parse(endDateStr);
                            if (endDate == null) continue;

                            if (endDate.before(today)) {
                                expiredList.add(name + " - Hết hạn: " + endDateStr);
                            } else if (endDate.before(oneMonthLater)) {
                                aboutToExpireList.add(name + " - Hết hạn: " + endDateStr);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    expiredAdapter.notifyDataSetChanged();
                    aboutToExpireAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> e.printStackTrace());
    }
}