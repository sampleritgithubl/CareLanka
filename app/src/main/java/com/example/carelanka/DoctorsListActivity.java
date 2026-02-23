package com.example.carelanka;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;
import com.google.firebase.firestore.*;
import java.util.ArrayList;

public class DoctorsListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Doctor> doctorList = new ArrayList<>();
    DoctorAdapter adapter;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_list);

        String specialty = getIntent().getStringExtra("specialty");

        recyclerView = findViewById(R.id.recyclerDoctors);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new DoctorAdapter(this, doctorList, doctor -> {
            Intent intent = new Intent(this, DoctorDetailsActivity.class);
            intent.putExtra("name", doctor.name);
            intent.putExtra("hospital", doctor.hospital);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        db.collection("doctors")
                .whereEqualTo("specialty", specialty)
                .get()
                .addOnSuccessListener(query -> {
                    for (DocumentSnapshot doc : query) {
                        doctorList.add(doc.toObject(Doctor.class));
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}