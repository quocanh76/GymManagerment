package com.example.gymmanagerment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {
    private List<Member> memberList;
    private MemberClickListener listener;

    public interface MemberClickListener {
        void onMemberClick(int position);
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public MemberAdapter(List<Member> memberList, MemberClickListener listener) {
        this.memberList = memberList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_member, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        Member member = memberList.get(position);
        holder.tvName.setText(member.getName());

        holder.itemView.setOnClickListener(v -> listener.onMemberClick(position));
        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(position));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(position));

    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    static class MemberViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        Button btnEdit, btnDelete;

        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
