package com.example.carelanka;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PatientDashboard extends AppCompatActivity {

    FirebaseAuth mAuth;
    TextView tvUserEmail, tvWelcomeUser;
    Button btnLogout;
    // XML එකේ ඇති අලුත් IDs වලට අනුව Variables සකස් කිරීම
    CardView cardFindCaregiver, cardDoctors, cardHospitals,cardMedicine ,cardEmergency;

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

        // අලුත් Card IDs සම්බන්ධ කිරීම
        cardFindCaregiver = findViewById(R.id.cardFindCaregiver);
        cardDoctors = findViewById(R.id.cardDoctors);
        cardHospitals = findViewById(R.id.cardHospitals);
        cardEmergency = findViewById(R.id.cardEmergency);
        cardMedicine = findViewById(R.id.cardMedicine);

        if(user != null){
            tvUserEmail.setText(user.getEmail());
        }

        // Click Listeners සැකසීම
        cardFindCaregiver.setOnClickListener(v -> {
            startActivity(new Intent(this, FindCaregiverActivity.class));
        });

        cardDoctors.setOnClickListener(v ->
                startActivity(new Intent(this, SpecialtiesActivity.class)));

        cardHospitals.setOnClickListener(v ->
                startActivity(new Intent(this, DistrictActivity.class)));

        cardEmergency.setOnClickListener(v ->
                startActivity(new Intent(this, EmergencyActivity.class)));

        cardMedicine.setOnClickListener(v -> {
            startActivity(new Intent(PatientDashboard.this, OnlinePharmacyActivity.class));
        });

        CardView cardReminder = findViewById(R.id.cardReminder);
        cardReminder.setOnClickListener(v -> startActivity(new Intent(this, ReminderActivity.class)));






        // Logout කිරීම
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(PatientDashboard.this, LoginActivity.class));
            finish();
        });
    }
}