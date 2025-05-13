package com.example.gymmanagerment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymmanagerment.Member;
import com.example.gymmanagerment.R;

import java.util.List;

public class MemberAttendanceAdapter extends RecyclerView.Adapter<MemberAttendanceAdapter.ViewHolder> {
    private List<Member> members;
    private OnMemberClickListener listener;
            public interface OnMemberClickListener {
            void onMemberClick(String memberId, String memberName); // Khi click vào item
            void onCheckinHistoryClick(String memberId); // Khi click nút lịch sử
            void onCheckinTodayClick(String memberId); // Phương thức mới thêm
        }

    public MemberAttendanceAdapter(List<Member> members, OnMemberClickListener listener) {
        this.members = members;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_member_attendance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Member member = members.get(position);

        holder.tvMemberName.setText(member.getName());
        holder.tvCheckinCount.setText(member.getCheckinCount() + " ngày");

        // Xử lý sự kiện click toàn bộ item
        holder.itemView.setOnClickListener(v ->
                listener.onMemberClick(member.getId(), member.getName()));

        // Xử lý sự kiện click nút lịch sử
        holder.btnCheckinHistory.setOnClickListener(v ->
                listener.onCheckinHistoryClick(member.getId()));

    }


    @Override
    public int getItemCount() {
        return members.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMemberName, tvCheckinCount, tvCheckinStatus;
        Button btnCheckinHistory;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMemberName = itemView.findViewById(R.id.tvMemberName);
            tvCheckinCount = itemView.findViewById(R.id.tvCheckinCount);
            tvCheckinStatus = itemView.findViewById(R.id.tvCheckinStatus);
            btnCheckinHistory = itemView.findViewById(R.id.btnCheckinHistory);

            itemView.setOnClickListener(v ->
                    listener.onCheckinTodayClick(members.get(getAdapterPosition()).getId()));
        }
    }
}