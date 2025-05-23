package com.example.gymmanagerment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RenewActivity extends AppCompatActivity {

    private static final int PAYMENT_REQUEST_CODE = 1001;
    private ListView listView;
    private ArrayList<QueryDocumentSnapshot> expiredMembers;
    private ArrayList<String> displayList;
    private ArrayAdapter<String> adapter;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;

    private String selectedDocId;
    private int selectedMonths;
    private long packagePrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renew);

        initializeViews();
        setupFirestore();
        loadExpiredMembers();
        setupListViewClick();
    }

    private void initializeViews() {
        listView = findViewById(R.id.lvExpiredMembers);
        db = FirebaseFirestore.getInstance();
        expiredMembers = new ArrayList<>();
        displayList = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
        listView.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang tải...");
        progressDialog.setCancelable(false);
    }

    private void setupFirestore() {
        db = FirebaseFirestore.getInstance();
    }

    private void setupListViewClick() {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            QueryDocumentSnapshot doc = expiredMembers.get(position);
            selectedDocId = doc.getId();
            showPackageSelectionDialog();
        });
    }

    private void loadExpiredMembers() {
        progressDialog.show();

        db.collection("Members")
                .get()
                .addOnSuccessListener(query -> {
                    displayList.clear();
                    expiredMembers.clear();
                    Date today = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                    for (QueryDocumentSnapshot doc : query) {
                        String endDateStr = doc.getString("end_date");
                        try {
                            Date endDate = sdf.parse(endDateStr);
                            if (endDate != null && endDate.before(today)) {
                                String memberInfo = String.format("%s\nSĐT: %s\nHết hạn: %s",
                                        doc.getString("name"),
                                        doc.getString("phone"),
                                        endDateStr);
                                displayList.add(memberInfo);
                                expiredMembers.add(doc);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Lỗi tải dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void showPackageSelectionDialog() {
        String[] packages = {"1 tháng","2 tháng", "3 tháng","4 tháng","5 tháng", "6 tháng","7 tháng","8 tháng","9 tháng","10 tháng","11 tháng", "12 tháng"};
        int[] months = {1,2,3,4,5,6,7,8,9,10,11, 12};

        new AlertDialog.Builder(this)
                .setTitle("Chọn gói gia hạn")
                .setItems(packages, (dialog, which) -> {
                    selectedMonths = months[which];
                    fetchPackagePrice(selectedMonths);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void fetchPackagePrice(int months) {
        progressDialog.show();

        db.collection("Packages")
                .document(String.valueOf(months))
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    progressDialog.dismiss();
                    if (documentSnapshot.exists()) {
                        packagePrice = documentSnapshot.getLong("price");
                        launchPaymentActivity();
                    } else {
                        Toast.makeText(this, "Gói tập không tồn tại", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void launchPaymentActivity() {
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra("AMOUNT", packagePrice);
        intent.putExtra("DESCRIPTION", "Gia hạn " + selectedMonths + " tháng");
        startActivityForResult(intent, PAYMENT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAYMENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                processRenewal();
            } else {
                Toast.makeText(this, "Thanh toán đã bị hủy", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void processRenewal() {
        progressDialog.show();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        String newStartDate = sdf.format(cal.getTime());
        cal.add(Calendar.MONTH, selectedMonths);
        String newEndDate = sdf.format(cal.getTime());

        Map<String, Object> updates = new HashMap<>();
        updates.put("start_date", newStartDate);
        updates.put("end_date", newEndDate);
        updates.put("package_id", String.valueOf(selectedMonths));
        updates.put("last_renewal", new Date());

        db.collection("Members").document(selectedDocId)
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Gia hạn thành công!", Toast.LENGTH_SHORT).show();
                    loadExpiredMembers();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Lỗi cập nhật: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}