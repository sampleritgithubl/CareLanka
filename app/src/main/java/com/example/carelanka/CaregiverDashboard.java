package com.example.carelanka;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class CaregiverDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver_dashboard);

        TextView tvUserEmail = findViewById(R.id.tvUserEmail);
        Button btnLogout = findViewById(R.id.btnLogout);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if(mAuth.getCurrentUser() != null){
            tvUserEmail.setText(mAuth.getCurrentUser().getEmail());
        }

        // Wallet Card එක ක්ලික් කළ විට මිල (Price) Update කිරීමට dialogue එකක් පෙන්වීම
        findViewById(R.id.cardWallet).setOnClickListener(v -> {
            showUpdatePriceDialog(mAuth.getUid(), db);
        });

        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void showUpdatePriceDialog(String uid, FirebaseFirestore db) {
        EditText etPrice = new EditText(this);
        etPrice.setHint("Enter your daily fee (LKR)");
        etPrice.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Update Your Fee")
                .setView(etPrice)
                .setPositiveButton("Update", (dialog, which) -> {
                    String price = etPrice.getText().toString();
                    if(!price.isEmpty()){
                        db.collection("Users").document(uid).update("price", price)
                                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Price Updated!", Toast.LENGTH_SHORT).show());
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
