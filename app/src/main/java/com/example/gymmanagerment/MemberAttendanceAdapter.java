package com.example.gymmanagerment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MemberAttendanceAdapter extends RecyclerView.Adapter<MemberAttendanceAdapter.ViewHolder> {

    private List<Member> members;
    private Context context;
    private OnMemberClickListener listener;

    public interface OnMemberClickListener {
        void onMemberClick(String memberId, String memberName);
        void onCheckinHistoryClick(String memberId);
        void onCheckinTodayClick(String memberId);
    }

    public MemberAttendanceAdapter(List<Member> members, Context context, OnMemberClickListener listener) {
        this.members = members;
        this.context = context;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMemberName, tvCheckinStatus, tvCheckinCount;
        Button btnCheckinHistory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMemberName = itemView.findViewById(R.id.tvMemberName);
            tvCheckinStatus = itemView.findViewById(R.id.tvCheckinStatus);
            tvCheckinCount = itemView.findViewById(R.id.tvCheckinCount);
            btnCheckinHistory = itemView.findViewById(R.id.btnCheckinHistory);
        }
    }

    @NonNull
    @Override
    public MemberAttendanceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_attendance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberAttendanceAdapter.ViewHolder holder, int position) {
        Member member = members.get(position);

        holder.tvMemberName.setText(member.getName());

        if (member.isCheckedInToday()) {
            holder.tvCheckinStatus.setText("Đã điểm danh");
            holder.tvCheckinStatus.setBackgroundResource(R.drawable.bg_checkin_status);
        } else {
            holder.tvCheckinStatus.setText("Chưa điểm danh");
            holder.tvCheckinStatus.setBackgroundResource(R.drawable.bg_uncheckin_status);
        }

        holder.tvCheckinCount.setText(member.getMonthlyCheckinCount() + " ngày");

        holder.btnCheckinHistory.setOnClickListener(v ->
                listener.onCheckinHistoryClick(member.getId()));

        holder.itemView.setOnClickListener(v ->
                listener.onMemberClick(member.getId(), member.getName()));
    }

    @Override
    public int getItemCount() {
        return members.size();
    }
}
