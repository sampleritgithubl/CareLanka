package com.example.carelanka;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpecialtyAdapter extends RecyclerView.Adapter<SpecialtyAdapter.ViewHolder> {

    private List<String> list;
    private OnClickListener listener;
    // විශේෂඥතාවට අදාළ අයිකන ගබඩා කිරීමට Map එකක්
    private Map<String, String> icons = new HashMap<>();

    public interface OnClickListener {
        void onClick(String specialty);
    }

    public SpecialtyAdapter(List<String> list, OnClickListener listener) {
        this.list = list;
        this.listener = listener;

        // එක් එක් වර්ගයට අදාළ Emoji අයිකන මෙහි සකසන්න
        icons.put("Cardiologist", "❤️");
        icons.put("Dermatologist", "✨");
        icons.put("Neurologist", "🧠");
        icons.put("Pediatrician", "👶");
        icons.put("Orthopedic", "🦴");
        icons.put("General Physician", "🩺");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // ඔබ සෑදූ අලුත් item_specialty.xml එක මෙහිදී සම්බන්ධ කරයි
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_specialty, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String specialty = list.get(position);

        // අයිකනය සහ නම ViewHolder එකට ලබා දීම
        holder.tvName.setText(specialty);
        holder.tvIcon.setText(icons.getOrDefault(specialty, "⚕️"));

        holder.itemView.setOnClickListener(v -> listener.onClick(specialty));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvIcon;

        ViewHolder(View itemView) {
            super(itemView);
            // item_specialty.xml හි ඇති IDs
            tvName = itemView.findViewById(R.id.tvSpecialtyName);
            tvIcon = itemView.findViewById(R.id.tvIcon);
        }
    }
}