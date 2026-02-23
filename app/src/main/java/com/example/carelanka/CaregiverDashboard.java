package com.example.carelanka;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class CaregiverDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // මෙයට වෙනම XML එකක් (activity_caregiver_dashboard) පසුව සෑදිය හැක.
        // දැනට පරීක්ෂා කිරීමට activity_dashboard ම පාවිච්චි කරමු.
        setContentView(R.layout.activity_caregiver_dashboard);

        TextView tvUserEmail = findViewById(R.id.tvUserEmail);
        Button btnLogout = findViewById(R.id.btnLogout);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            tvUserEmail.setText("Caregiver: " + mAuth.getCurrentUser().getEmail());
        }

        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(CaregiverDashboard.this, LoginActivity.class));
            finish();
        });

        Toast.makeText(this, "Welcome to Caregiver Dashboard", Toast.LENGTH_LONG).show();
    }
}