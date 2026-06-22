package com.example.carelanka;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import java.util.List;

public class EmergencyAdapter extends RecyclerView.Adapter<EmergencyAdapter.ViewHolder> {

    private List<EmergencyActivity.EmergencyContact> list;
    private OnCallClickListener listener;

    public interface OnCallClickListener {
        void onCallClick(String number);
    }

    public EmergencyAdapter(List<EmergencyActivity.EmergencyContact> list, OnCallClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emergency, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EmergencyActivity.EmergencyContact contact = list.get(position);
        holder.name.setText(contact.name);
        holder.number.setText(contact.number);
        holder.icon.setText(contact.icon);

        // Dynamic Color Logic based on Emoji
        int color;
        switch (contact.icon) {
            case "🚑": color = Color.parseColor("#D32F2F"); break;
            case "👮": color = Color.parseColor("#1565C0"); break;
            case "🚒": color = Color.parseColor("#E65100"); break;
            case "🧠": color = Color.parseColor("#6A1B9A"); break;
            case "👶": color = Color.parseColor("#AD1457"); break;
            case "⚡": color = Color.parseColor("#F57F17"); break;
            default: color = Color.parseColor("#D32F2F");
        }

        holder.leftBorder.setBackgroundColor(color);
        holder.iconContainer.setCardBackgroundColor(color);

        // Click Listeners (Entire card + button)
        holder.itemView.setOnClickListener(v -> listener.onCallClick(contact.number));
        holder.btnCall.setOnClickListener(v -> listener.onCallClick(contact.number));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, number, icon;
        MaterialButton btnCall;
        View leftBorder;
        MaterialCardView iconContainer;

        public ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.tvEmergencyName);
            number = v.findViewById(R.id.tvEmergencyNumber);
            icon = v.findViewById(R.id.tvEmergencyIcon);
            btnCall = v.findViewById(R.id.btnCallNow);
            leftBorder = v.findViewById(R.id.leftBorder);
            iconContainer = v.findViewById(R.id.iconContainer);
        }
    }
}
