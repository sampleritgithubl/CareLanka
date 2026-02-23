package com.example.carelanka;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class ReminderActivity extends AppCompatActivity {

    EditText etMedName;
    TextView tvSelectedTime;
    Button btnSelectTime, btnSetReminder;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        etMedName = findViewById(R.id.etMedName);
        tvSelectedTime = findViewById(R.id.tvSelectedTime);
        btnSelectTime = findViewById(R.id.btnSelectTime);
        btnSetReminder = findViewById(R.id.btnSetReminder);
        calendar = Calendar.getInstance();

        btnSelectTime.setOnClickListener(v -> {
            TimePickerDialog timePicker = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                tvSelectedTime.setText("Selected Time: " + hourOfDay + ":" + String.format("%02d", minute));
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
            timePicker.show();
        });

        btnSetReminder.setOnClickListener(v -> {
            String medName = etMedName.getText().toString();
            if (medName.isEmpty()) {
                Toast.makeText(this, "Please enter medicine name", Toast.LENGTH_SHORT).show();
                return;
            }

            setAlarm(medName);
        });
    }

    private void setAlarm(String medName) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("med_name", medName);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Toast.makeText(this, "Reminder set for " + medName, Toast.LENGTH_SHORT).show();
        }
    }
}