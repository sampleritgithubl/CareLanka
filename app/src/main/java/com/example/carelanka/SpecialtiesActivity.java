package com.example.carelanka;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Arrays;
import java.util.List;

public class SpecialtiesActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialties);

        recyclerView = findViewById(R.id.recyclerSpecialties);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<String> specialties = Arrays.asList(
                "Cardiologist",
                "Dermatologist",
                "Neurologist",
                "Pediatrician",
                "Orthopedic"
        );

        recyclerView.setAdapter(new SpecialtyAdapter(specialties, specialty -> {
            Intent intent = new Intent(this, DoctorsListActivity.class);
            intent.putExtra("specialty", specialty);
            startActivity(intent);
        }));
    }
}