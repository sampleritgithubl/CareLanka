package com.example.carelanka;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class EmergencyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        RecyclerView recyclerView = findViewById(R.id.recyclerEmergency);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // හදිසි ඇමතුම් ලැයිස්තුව
        List<EmergencyContact> contactList = new ArrayList<>();
        contactList.add(new EmergencyContact("Suwa Seriya Ambulance", "1990", "🚑"));
        contactList.add(new EmergencyContact("Police", "119", "👮"));
        contactList.add(new EmergencyContact("Fire Brigade", "110", "🚒"));
        contactList.add(new EmergencyContact("Mental Health Help", "1926", "🧠"));
        contactList.add(new EmergencyContact("Child Protection", "1929", "👶"));
        contactList.add(new EmergencyContact("Disaster Management", "117", "⚡"));

        // Adapter එක සම්බන්ධ කිරීම
        EmergencyAdapter adapter = new EmergencyAdapter(contactList, number -> {
            // දුරකථන ඇමතුම ලබා ගැනීමට Intent එකක් යැවීම
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + number));
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
    }

    // Model Class එක
    public static class EmergencyContact {
        String name, number, icon;
        public EmergencyContact(String name, String number, String icon) {
            this.name = name; this.number = number; this.icon = icon;
        }
    }
}