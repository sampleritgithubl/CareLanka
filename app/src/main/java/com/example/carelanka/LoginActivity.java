package com.example.carelanka;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin;
    private ProgressBar progressBar;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // ViewModel එක සම්බන්ධ කිරීම
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        // XML එකේ progressBar එකක් නැතිනම් පහත පේළිය ඉවත් කරන්න හෝ එක් කරන්න
        // progressBar = findViewById(R.id.progressBar); 

        // LiveData නිරීක්ෂණය කිරීම (Observers)
        setupObservers();

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.login(email, password);
        });

        findViewById(R.id.tvSignUp).setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    private void setupObservers() {
        // Login සාර්ථක වූ පසු Role එක අනුව Dashboard එකට යෑම
        viewModel.getUserRole().observe(this, role -> {
            if (role != null) {
                if ("Caregiver".equals(role)) {
                    startActivity(new Intent(this, CaregiverDashboard.class));
                } else {
                    startActivity(new Intent(this, PatientDashboard.class));
                }
                finish();
            }
        });

        // Error එකක් ආවොත් පෙන්වීම
        viewModel.getLoginError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });

        // Loading ස්වභාවය පාලනය (Optional)
        viewModel.getIsLoading().observe(this, isLoading -> {
            if (progressBar != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
            btnLogin.setEnabled(!isLoading);
        });
    }
}
