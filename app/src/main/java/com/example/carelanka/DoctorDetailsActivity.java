package com.example.carelanka;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class DoctorDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        String name = getIntent().getStringExtra("name");
        String hospital = getIntent().getStringExtra("hospital");

        TextView tvName = findViewById(R.id.tvDoctorName);
        TextView tvHospital = findViewById(R.id.tvHospital);
        Button btnBook = findViewById(R.id.btnBook);

        tvName.setText(name);
        tvHospital.setText(hospital);

        btnBook.setOnClickListener(v -> {

            Map<String, Object> booking = new HashMap<>();
            booking.put("doctor", name);
            booking.put("hospital", hospital);
            booking.put("status", "Pending");

            FirebaseFirestore.getInstance()
                    .collection("appointments")
                    .add(booking);

            Toast.makeText(this, "Appointment Booked!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}