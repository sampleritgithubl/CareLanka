package com.example.carelanka;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;
import com.google.firebase.firestore.*;
import java.util.ArrayList;

public class FindCaregiverActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    CaregiverAdapter adapter;
    // Change Caregiver to User here
    ArrayList<User> list;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_list);

        recyclerView = findViewById(R.id.recyclerDoctors);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new CaregiverAdapter(this, list);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .whereEqualTo("role", "Caregiver")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        // Change Caregiver.class to User.class here
                        list.add(doc.toObject(User.class));
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}