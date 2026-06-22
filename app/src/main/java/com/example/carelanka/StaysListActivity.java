package com.example.carelanka;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class StaysListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StayAdapter adapter;
    private ArrayList<Stay> stayList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stays_list);

        db = FirebaseFirestore.getInstance();
        
        recyclerView = findViewById(R.id.recyclerStays);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        adapter = new StayAdapter(this, stayList);
        recyclerView.setAdapter(adapter);

        // Near Me functionality using Google Maps
        View cardNearMe = findViewById(R.id.cardStaysNearMe);
        if (cardNearMe != null) {
            cardNearMe.setOnClickListener(v -> {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode("medical accommodation guest house near me"));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(this, "Google Maps not found", Toast.LENGTH_SHORT).show();
                }
            });
        }

        loadStays();
    }

    private void loadStays() {
        db.collection("stays")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    stayList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        stayList.add(doc.toObject(Stay.class));
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
