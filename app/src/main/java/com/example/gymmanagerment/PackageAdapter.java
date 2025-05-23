package com.example.gymmanagerment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_package, parent, false);
        return new PackageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PackageViewHolder holder, int position) {
        Package pkg = packageList.get(position);
        holder.txtPackageName.setText(pkg.getName());
        holder.txtPackageDetails.setText("Gói tập " + pkg.getDuration() + " tháng");
        holder.txtDuration.setText(pkg.getDuration() + " tháng");
        holder.txtPackagePrice.setText(formatPrice(pkg.getPrice()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MembersByPackageActivity.class);
            intent.putExtra("packageId", pkg.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return packageList.size();
    }

    public static class PackageViewHolder extends RecyclerView.ViewHolder {
        TextView txtPackageName, txtPackageDetails, txtDuration, txtPackagePrice;

        public PackageViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPackageName = itemView.findViewById(R.id.txtPackageName);
            txtPackageDetails = itemView.findViewById(R.id.txtPackageDetails);
            txtDuration = itemView.findViewById(R.id.txtDuration);
            txtPackagePrice = itemView.findViewById(R.id.txtPackagePrice);
        }
    }

    private String formatPrice(int price) {
        return String.format("%,dđ", price).replace(",", ".");
    }
}
