package com.example.carelanka;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
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
        holder.btnCall.setOnClickListener(v -> listener.onCallClick(contact.number));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, number, icon;
        MaterialButton btnCall;
        public ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.tvEmergencyName);
            number = v.findViewById(R.id.tvEmergencyNumber);
            icon = v.findViewById(R.id.tvEmergencyIcon);
            btnCall = v.findViewById(R.id.btnCallNow);
        }
    }
}