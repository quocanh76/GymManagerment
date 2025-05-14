package com.example.gymmanagerment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {
    private List<AttendanceRecord> records;

    public AttendanceAdapter(List<AttendanceRecord> records) {
        this.records = records;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_attendance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AttendanceRecord record = records.get(position);
        holder.txtName.setText(record.getMemberName());
        holder.txtDate.setText(record.getDate());
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtDate;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
        }
    }
}
