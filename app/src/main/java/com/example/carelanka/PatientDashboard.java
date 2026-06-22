package com.example.carelanka;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.WorkInfo;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PatientDashboard extends AppCompatActivity {

    FirebaseAuth mAuth;
    TextView tvUserName;
    MaterialButton btnAiChat;
    MaterialCardView cardFindCaregiver, cardDoctors, cardHospitals, cardMedicine, cardEmergency, cardReminder, cardAccommodation;
    MaterialCardView cardPrescription, cardSkinAnalysis;
    private ModelDownloadManager downloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        tvUserName = findViewById(R.id.tvUserName);
        btnAiChat = findViewById(R.id.btnAiChat);

        if(user != null && user.getEmail() != null){
            String name = user.getEmail().split("@")[0];
            tvUserName.setText(name.substring(0, 1).toUpperCase() + name.substring(1));
        }

        // AI Bot Click Action - Now opens the Multilingual AI Assistant
        btnAiChat.setOnClickListener(v -> {
            startActivity(new Intent(this, MultilingualAIActivity.class));
        });

        setupClickListeners();
        
        // Initialize Language Model Download
        initLanguageModels();
    }

    private void initLanguageModels() {
        downloadManager = new ModelDownloadManager(this);
        if (!downloadManager.areModelsReady()) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), 
                    "Downloading language support...", Snackbar.LENGTH_INDEFINITE);
            snackbar.show();

            downloadManager.downloadModels().observe(this, workInfo -> {
                if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                    snackbar.dismiss();
                    Snackbar.make(findViewById(android.R.id.content), 
                            "Sinhala and Tamil support ready!", Snackbar.LENGTH_SHORT).show();
                } else if (workInfo != null && workInfo.getState() == WorkInfo.State.FAILED) {
                    snackbar.dismiss();
                    Toast.makeText(this, "Language download failed.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setupClickListeners() {
        if (findViewById(R.id.cardFindCaregiver) != null) {
            findViewById(R.id.cardFindCaregiver).setOnClickListener(v -> startActivity(new Intent(this, FindCaregiverActivity.class)));
        }
        if (findViewById(R.id.cardDoctors) != null) {
            findViewById(R.id.cardDoctors).setOnClickListener(v -> startActivity(new Intent(this, SpecialtiesActivity.class)));
        }
        if (findViewById(R.id.cardHospitals) != null) {
            findViewById(R.id.cardHospitals).setOnClickListener(v -> startActivity(new Intent(this, DistrictActivity.class)));
        }
        if (findViewById(R.id.cardEmergency) != null) {
            findViewById(R.id.cardEmergency).setOnClickListener(v -> startActivity(new Intent(this, EmergencyActivity.class)));
        }
        if (findViewById(R.id.cardMedicine) != null) {
            findViewById(R.id.cardMedicine).setOnClickListener(v -> startActivity(new Intent(this, OnlinePharmacyActivity.class)));
        }
        if (findViewById(R.id.cardReminder) != null) {
            findViewById(R.id.cardReminder).setOnClickListener(v -> startActivity(new Intent(this, ReminderActivity.class)));
        }
        if (findViewById(R.id.cardAccommodation) != null) {
            findViewById(R.id.cardAccommodation).setOnClickListener(v -> startActivity(new Intent(this, StaysListActivity.class)));
        }
        if (findViewById(R.id.cardPrescription) != null) {
            findViewById(R.id.cardPrescription).setOnClickListener(v -> startActivity(new Intent(this, PrescriptionAIActivity.class)));
        }
        if (findViewById(R.id.cardSkinAnalysis) != null) {
            findViewById(R.id.cardSkinAnalysis).setOnClickListener(v -> startActivity(new Intent(this, SkinAnalysisActivity.class)));
        }
    }
}
