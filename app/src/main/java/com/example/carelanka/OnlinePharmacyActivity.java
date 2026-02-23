package com.example.carelanka;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class OnlinePharmacyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_pharmacy);

        // UI Cards සම්බන්ධ කිරීම
        CardView cardHealthGuard = findViewById(R.id.cardHealthGuard);
        CardView cardLaugfs = findViewById(R.id.cardLaugfs);
        CardView cardQuickMed = findViewById(R.id.cardQuickMed);
        CardView cardPickMeFlash = findViewById(R.id.cardPickMeFlash);

        // Click Listeners
        cardHealthGuard.setOnClickListener(v -> openUrl("https://healthguard.lk/"));
        cardLaugfs.setOnClickListener(v -> openUrl("https://laugfswellness.com/"));
        cardQuickMed.setOnClickListener(v -> openUrl("https://quickmed.lk/"));

        // PickMe Flash (මෙය PickMe App එක ඇත්නම් එය විවෘත කිරීමට උත්සාහ කරයි)
        cardPickMeFlash.setOnClickListener(v -> openUrl("https://pickme.lk/flash"));
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}