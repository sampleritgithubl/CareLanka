package com.example.carelanka;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;

public class StayAdapter extends RecyclerView.Adapter<StayAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Stay> list;

    public StayAdapter(Context context, ArrayList<Stay> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_stay, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Stay stay = list.get(position);
        holder.name.setText(stay.name);
        holder.location.setText(stay.location);
        holder.price.setText("LKR " + stay.price + " / Night");

        holder.btnCall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + stay.phone));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, location, price;
        MaterialButton btnCall;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvStayName);
            location = itemView.findViewById(R.id.tvStayLocation);
            price = itemView.findViewById(R.id.tvStayPrice);
            btnCall = itemView.findViewById(R.id.btnStayCall);
        }
    }
}
