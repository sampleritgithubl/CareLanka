package com.example.carelanka;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CaregiverAdapter extends RecyclerView.Adapter<CaregiverAdapter.ViewHolder> {
    Context context;
    ArrayList<User> list;

    public CaregiverAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_caregiver, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = list.get(position);
        holder.name.setText(user.name);
        holder.rating.setText("⭐ " + (user.rating != null ? user.rating : "5.0"));
        holder.price.setText("LKR " + (user.price != null ? user.price : "0") + " / Day");

        holder.btnCall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + user.phone));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, rating, price;
        Button btnCall;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvName);
            rating = itemView.findViewById(R.id.tvRating);
            price = itemView.findViewById(R.id.tvPrice);
            btnCall = itemView.findViewById(R.id.btnCall);
        }
    }
}