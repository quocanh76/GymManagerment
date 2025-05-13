package com.example.gymmanagerment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AttendanceHistoryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<String> checkinList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    FirebaseFirestore db;
    String memberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        recyclerView = findViewById(R.id.recyclerViewCheckin);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, checkinList);
        db = FirebaseFirestore.getInstance();

        memberId = getIntent().getStringExtra("memberId");
        loadCheckInData();
    }

    private void loadCheckInData() {
        db.collection("Attendance")
                .whereEqualTo("memberId", memberId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    checkinList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        String time = doc.getString("checkin_time");
                        checkinList.add(time);
                    }
                    recyclerView.setAdapter(new ArrayAdapterAdapter(this, checkinList));
                });
    }

    class ArrayAdapterAdapter extends RecyclerView.Adapter<ArrayAdapterAdapter.ViewHolder> {
        Context context;
        List<String> times;

        ArrayAdapterAdapter(Context context, List<String> times) {
            this.context = context;
            this.times = times;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView timeView;
            ViewHolder(View itemView) {
                super(itemView);
                timeView = itemView.findViewById(android.R.id.text1);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.timeView.setText(times.get(position));
        }

        @Override
        public int getItemCount() {
            return times.size();
        }
    }

}
