package com.example.carelanka;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DistrictAdapter extends RecyclerView.Adapter<DistrictAdapter.ViewHolder> {

    private List<String> list;
    private OnClickListener listener;

    public interface OnClickListener {
        void onClick(String district);
    }

    public DistrictAdapter(List<String> list, OnClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_district, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String district = list.get(position);
        holder.tvName.setText(district);
        // දිස්ත්‍රික්ක සඳහා පොදු වෘත්තීය අයිකනයක් (🏢) භාවිතා කරයි
        holder.tvIcon.setText("🏢");

        holder.itemView.setOnClickListener(v -> listener.onClick(district));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvIcon;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvSpecialtyName);
            tvIcon = itemView.findViewById(R.id.tvIcon);
        }
    }
}
