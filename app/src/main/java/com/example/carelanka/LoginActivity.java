package com.example.carelanka;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    TextView tvRegister;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // 1. Firebase Auth හරහා ලොග් වීම
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String uid = mAuth.getCurrentUser().getUid();

                            // 2. Firestore වෙතින් Role එක පරීක්ෂා කිරීම
                            db.collection("Users").document(uid).get()
                                    .addOnCompleteListener(dbTask -> {
                                        if (dbTask.isSuccessful()) {
                                            DocumentSnapshot document = dbTask.getResult();
                                            if (document.exists()) {
                                                // Firestore හි ඇති "role" අගය ලබා ගැනීම
                                                String role = document.getString("role");

                                                if ("Caregiver".equals(role)) {
                                                    Toast.makeText(this, "Welcome Caregiver!", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(LoginActivity.this, CaregiverDashboard.class));
                                                } else {
                                                    Toast.makeText(this, "Welcome Patient!", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(LoginActivity.this, PatientDashboard.class));
                                                }
                                                finish();
                                            } else {
                                                Toast.makeText(this, "User details not found in database", Toast.LENGTH_SHORT).show();
                                                mAuth.signOut();
                                            }
                                        } else {
                                            Toast.makeText(this, "Database Error: " + dbTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        } else {
                            Toast.makeText(this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
}
