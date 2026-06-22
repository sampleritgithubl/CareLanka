package com.example.carelanka;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;

public class EmergencyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        // SOS Button Setup
        MaterialButton btnSosPanic = findViewById(R.id.btnSosPanic);
        Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
        btnSosPanic.startAnimation(pulse);

        btnSosPanic.setOnClickListener(v -> showSosConfirmationDialog());

        // Existing RecyclerView Setup
        RecyclerView recyclerView = findViewById(R.id.recyclerEmergency);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<EmergencyContact> contactList = new ArrayList<>();
        contactList.add(new EmergencyContact("Suwa Seriya Ambulance", "1990", "🚑"));
        contactList.add(new EmergencyContact("Police", "119", "👮"));
        contactList.add(new EmergencyContact("Fire Brigade", "110", "🚒"));
        contactList.add(new EmergencyContact("Mental Health Help", "1926", "🧠"));
        contactList.add(new EmergencyContact("Child Protection", "1929", "👶"));
        contactList.add(new EmergencyContact("Disaster Management", "117", "⚡"));

        EmergencyAdapter adapter = new EmergencyAdapter(contactList, number -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + number));
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
    }

    private void showSosConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("🚨 SEND SOS ALERT")
                .setMessage("Send SOS alert to your emergency contacts?")
                .setPositiveButton("YES", (dialog, which) -> {
                    Intent intent = new Intent(this, SosPanicActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton("NO", null)
                .show();
    }

    public static class EmergencyContact {
        String name, number, icon;
        public EmergencyContact(String name, String number, String icon) {
            this.name = name; this.number = number; this.icon = icon;
        }
    }
}
