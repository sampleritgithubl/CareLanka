package com.example.carelanka;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Arrays;
import java.util.List;

public class DistrictActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CardView cardNearMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district);

        // UI සම්බන්ධ කිරීම
        recyclerView = findViewById(R.id.recyclerDistricts);
        cardNearMe = findViewById(R.id.cardNearMe);

        // RecyclerView සැකසීම
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // දිස්ත්‍රික්ක ලැයිස්තුව
        List<String> districts = Arrays.asList(
                "Ampara", "Anuradhapura", "Badulla", "Batticaloa", "Colombo",
                "Galle", "Gampaha", "Hambantota", "Jaffna", "Kalutara",
                "Kandy", "Kegalle", "Kilinochchi", "Kurunegala", "Mannar",
                "Matale", "Matara", "Moneragala", "Mullaitivu", "Nuwara Eliya",
                "Polonnaruwa", "Puttalam", "Ratnapura", "Trincomalee", "Vavuniya"
        );

        // දිස්ත්‍රික්ක සඳහා Adapter එක සම්බන්ධ කිරීම
        recyclerView.setAdapter(new SpecialtyAdapter(districts, district -> {
            Intent intent = new Intent(this, HospitalListActivity.class);
            intent.putExtra("district", district);
            startActivity(intent);
        }));

        // --- Near Me Button ක්‍රියාවලිය ---
        cardNearMe.setOnClickListener(v -> {
            String[] options = {"Government Hospitals", "Private Hospitals", "All Hospitals"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("What are you looking for?");
            builder.setItems(options, (dialog, which) -> {
                String searchQuery = "";

                if (which == 0) {
                    searchQuery = "government hospitals near me";
                } else if (which == 1) {
                    searchQuery = "private hospitals near me";
                } else {
                    searchQuery = "hospitals near me";
                }

                // Google Maps විවෘත කිරීම
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(searchQuery));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(this, "Google Maps not found", Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();
        });
    }
}