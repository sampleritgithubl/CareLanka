package com.example.carelanka;

import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.ViewHolder> {
    ArrayList<Hospital> list;

    public HospitalAdapter(ArrayList<Hospital> list) { this.list = list; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hospital, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hospital h = list.get(position);
        holder.name.setText(h.name);
        holder.address.setText(h.address);
    }

    @Override
    public int getItemCount() { return list.size(); }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, address;
        public ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.tvHospitalName);
            address = v.findViewById(R.id.tvHospitalAddress);
        }
    }
}