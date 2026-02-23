package com.example.carelanka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {

    private Context context;
    private ArrayList<Doctor> doctorList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Doctor doctor);
    }

    public DoctorAdapter(Context context, ArrayList<Doctor> doctorList, OnItemClickListener listener) {
        this.context = context;
        this.doctorList = doctorList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // මෙහිදී ඔබ සෑදූ item_doctor.xml එක සම්බන්ධ කරයි
        View view = LayoutInflater.from(context).inflate(R.layout.item_doctor, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor doctor = doctorList.get(position);
        holder.txtName.setText(doctor.name);
        holder.txtHospital.setText(doctor.hospital);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(doctor));
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public static class DoctorViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtHospital;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            // item_doctor.xml හි ඇති IDs මෙහි ලබා දෙන්න
            txtName = itemView.findViewById(R.id.textDoctorName);
            txtHospital = itemView.findViewById(R.id.textHospital);
        }
    }
}