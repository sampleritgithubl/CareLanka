package com.example.carelanka;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PatientDashboard extends AppCompatActivity {

    FirebaseAuth mAuth;
    TextView tvUserEmail, tvWelcomeUser;
    Button btnLogout;
    MaterialCardView cardFindCaregiver, cardDoctors, cardHospitals, cardMedicine, cardEmergency, cardReminder, cardAccommodation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        // UI සම්බන්ධ කිරීම
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvWelcomeUser = findViewById(R.id.tvWelcomeUser);
        btnLogout = findViewById(R.id.btnLogout);

        cardFindCaregiver = findViewById(R.id.cardFindCaregiver);
        cardDoctors = findViewById(R.id.cardDoctors);
        cardHospitals = findViewById(R.id.cardHospitals);
        cardEmergency = findViewById(R.id.cardEmergency);
        cardMedicine = findViewById(R.id.cardMedicine);
        cardReminder = findViewById(R.id.cardReminder);
        cardAccommodation = findViewById(R.id.cardAccommodation);

        if(user != null){
            tvUserEmail.setText(user.getEmail());
        }

        cardFindCaregiver.setOnClickListener(v -> startActivity(new Intent(this, FindCaregiverActivity.class)));
        cardDoctors.setOnClickListener(v -> startActivity(new Intent(this, SpecialtiesActivity.class)));
        cardHospitals.setOnClickListener(v -> startActivity(new Intent(this, DistrictActivity.class)));
        cardEmergency.setOnClickListener(v -> startActivity(new Intent(this, EmergencyActivity.class)));
        cardMedicine.setOnClickListener(v -> startActivity(new Intent(this, OnlinePharmacyActivity.class)));
        cardReminder.setOnClickListener(v -> startActivity(new Intent(this, ReminderActivity.class)));

        // --- Stays / Accommodation (Google Maps හරහා අවට ඇති නවාතැන් සෙවීම) ---
        cardAccommodation.setOnClickListener(v -> {
            // "hotels near me" ලෙස Google Maps වෙත query එකක් යැවීම
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=hotels+near+me");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                Toast.makeText(this, "Google Maps not found", Toast.LENGTH_SHORT).show();
            }
        });

        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(PatientDashboard.this, LoginActivity.class));
            finish();
        });
    }
}
