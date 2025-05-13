package com.example.gymmanagerment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.PackageViewHolder> {
    private List<Package> packageList;
    private Context context;

    public PackageAdapter(List<Package> packageList, Context context) {
        this.packageList = packageList;
        this.context = context;
    }

    @NonNull
    @Override
    public PackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.package_item, parent, false);
        return new PackageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PackageViewHolder holder, int position) {
        Package pack = packageList.get(position);
        holder.txtName.setText(pack.getName());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MembersByPackageActivity.class);
            intent.putExtra("packageId", pack.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return packageList.size();
    }

    public static class PackageViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;

        public PackageViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtPackageName);
        }
    }

}

