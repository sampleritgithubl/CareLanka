package com.example.carelanka;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    EditText etName, etEmail, etPhone, etPassword;
    RadioGroup rgRole;
    Button btnRegister;
    TextView tvLogin;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Firebase මූලිකාංග ආරම්භ කිරීම
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // UI සම්බන්ධ කිරීම
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        rgRole = findViewById(R.id.rgRole);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Role එක ලබා ගැනීම
            int selectedId = rgRole.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Please select a role", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton rb = findViewById(selectedId);
            String role = rb.getText().toString();

            // හිස් තැන් පරීක්ෂාව
            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            // 1. Firebase Auth හරහා ගිණුම සෑදීම
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String uid = mAuth.getCurrentUser().getUid();

                            // 2. පරිශීලක දත්ත Firestore හි ගබඩා කිරීම
                            User newUser = new User(uid, name, email, phone, role);

                            db.collection("Users").document(uid)
                                    .set(newUser)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(this, "Registration Successful! Please Login.", Toast.LENGTH_SHORT).show();

                                        // ඉතාම වැදගත්: Sign Out කර Login එකට යැවිය යුතුයි
                                        mAuth.signOut();

                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Firestore Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(this, "Auth Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }
}