package com.example.carelanka;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;
import com.google.firebase.firestore.*;
import java.util.ArrayList;

public class FindCaregiverActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    CaregiverAdapter adapter;
    ArrayList<User> list;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_list);

        // 1. ActionBar එකේ Title එක වෙනස් කිරීම
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Available Caregivers");
        }

        // 2. XML එකේ ඇති TextView එක සොයාගෙන නම වෙනස් කිරීම
        TextView tvTitle = findViewById(R.id.tvDoctorsListTitle);
        if (tvTitle != null) {
            tvTitle.setText("Available Caregivers");
        }

        // RecyclerView එක සම්බන්ධ කිරීම
        recyclerView = findViewById(R.id.recyclerDoctors);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new CaregiverAdapter(this, list);
        recyclerView.setAdapter(adapter);

        // Firestore එකෙන් Caregivers ලා පමණක් ලබා ගැනීම
        db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .whereEqualTo("role", "Caregiver")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    list.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        list.add(doc.toObject(User.class));
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}