package com.example.carelanka;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.*;
import java.util.ArrayList;

public class HospitalListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    HospitalAdapter adapter;
    ArrayList<Hospital> hospitalList = new ArrayList<>();
    FirebaseFirestore db;
    String selectedDistrict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_list);

        selectedDistrict = getIntent().getStringExtra("district");
        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerHospitals);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HospitalAdapter(hospitalList);
        recyclerView.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);

        // මුලින්ම Government රෝහල් පෙන්වන්න
        loadHospitals("Government");

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                loadHospitals(tab.getText().toString());
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void loadHospitals(String type) {
        db.collection("hospitals")
                .whereEqualTo("district", selectedDistrict)
                .whereEqualTo("type", type)
                .get()
                .addOnSuccessListener(query -> {
                    hospitalList.clear();
                    for (DocumentSnapshot doc : query) {
                        hospitalList.add(doc.toObject(Hospital.class));
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}