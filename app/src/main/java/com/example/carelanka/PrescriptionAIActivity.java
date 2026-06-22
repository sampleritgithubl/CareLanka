package com.example.carelanka;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PrescriptionAIActivity extends AppCompatActivity {

    private static final String TAG = "PrescriptionAI";

    // ✅ Key එක BuildConfig එකෙන් එනවා, code එකේ hardcode කරලා නෑ.
    // local.properties එකේ GEMINI_API_KEY=... කියලා දාන්න.
    private static final String API_KEY = BuildConfig.GEMINI_API_KEY;

    // ✅ Current, supported model (gemini-1.5-flash දැන් shut down වෙලා)
    private static final String MODEL_NAME = "gemini-2.5-flash";

    private static final String GEMINI_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/"
                    + MODEL_NAME + ":generateContent?key=" + API_KEY;

    private static final String SYSTEM_INSTRUCTION =
            "You are a professional medical assistant. Analyze the prescription image. " +
                    "1. Extract medicine names, dosages, and usage instructions in Sinhala. " +
                    "2. IMPORTANT: At the very end of your response, provide a structured list of medications and their recommended times in 24-hour format (HH:mm) enclosed in [REMINDERS] tags. " +
                    "Example format: [REMINDERS] {\"reminders\": [{\"med\": \"Panadol\", \"time\": \"08:00\"}, {\"med\": \"Amoxicillin\", \"time\": \"20:00\"}]} [/REMINDERS] " +
                    "Guess reasonable times if not exact (Morning: 08:00, Noon: 13:00, Night: 20:00).";

    private static final String USER_PROMPT =
            "මේ prescription එකේ medicine විස්තර සිංහලෙන් දෙන්න. වෙලාවල් [REMINDERS] tag එක ඇතුළෙත් දාන්න.";

    private ImageView ivPrescription;
    private TextView tvResult;
    private ProgressBar progressBar;
    private View cardResult;
    private Button btnAnalyze;
    private Bitmap selectedBitmap;

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();

    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_ai);

        ivPrescription = findViewById(R.id.ivPrescription);
        tvResult = findViewById(R.id.tvResult);
        progressBar = findViewById(R.id.progressBar);
        cardResult = findViewById(R.id.cardResult);
        btnAnalyze = findViewById(R.id.btnAnalyze);
        Button btnPickImage = findViewById(R.id.btnPickImage);
        Button btnManualReminder = findViewById(R.id.btnManualReminder);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            Bitmap original = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                            selectedBitmap = resizeBitmap(original, 1024);
                            ivPrescription.setImageBitmap(selectedBitmap);
                            ivPrescription.setAlpha(1.0f);
                            btnAnalyze.setEnabled(true);
                        } catch (IOException e) {
                            Log.e(TAG, "Image load failed", e);
                            Toast.makeText(this, "Image load failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        btnPickImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(intent);
        });

        btnAnalyze.setOnClickListener(v -> analyzePrescription());

        if (btnManualReminder != null) {
            btnManualReminder.setOnClickListener(v -> {
                Intent intent = new Intent(PrescriptionAIActivity.this, ReminderActivity.class);
                startActivity(intent);
            });
        }
    }

    private Bitmap resizeBitmap(Bitmap original, int maxSize) {
        int width = original.getWidth();
        int height = original.getHeight();
        float ratio = (float) width / (float) height;
        if (ratio > 1) {
            width = maxSize;
            height = (int) (width / ratio);
        } else {
            height = maxSize;
            width = (int) (height * ratio);
        }
        return Bitmap.createScaledBitmap(original, width, height, true);
    }

    private void analyzePrescription() {
        if (selectedBitmap == null) return;

        if (API_KEY == null || API_KEY.isEmpty()) {
            showError("API key not configured. local.properties එකේ GEMINI_API_KEY දාලාද බලන්න.");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnAnalyze.setEnabled(false);
        cardResult.setVisibility(View.GONE);

        try {
            String base64Image = bitmapToBase64(selectedBitmap);
            JSONObject requestBody = buildRequestBody(base64Image);

            RequestBody body = RequestBody.create(
                    requestBody.toString(),
                    MediaType.parse("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(GEMINI_URL)
                    .post(body)
                    .build();

            httpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "Network call failed", e);
                    mainHandler.post(() -> showError("Network error: " + e.getMessage()));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseBody = response.body() != null ? response.body().string() : "";

                    if (!response.isSuccessful()) {
                        Log.e(TAG, "API error " + response.code() + ": " + responseBody);
                        mainHandler.post(() -> showError(
                                "API Error (" + response.code() + "). Check Logcat for details."));
                        return;
                    }

                    try {
                        String text = extractTextFromResponse(responseBody);
                        mainHandler.post(() -> parseRemindersAndSetAlarms(text));
                    } catch (Exception e) {
                        Log.e(TAG, "Failed to parse Gemini response", e);
                        mainHandler.post(() -> showError("Response parsing failed."));
                    }
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Request build failed", e);
            showError("Request build failed: " + e.getMessage());
        }
    }

    /** Builds the Gemini REST API request body (systemInstruction + image + text parts). */
    private JSONObject buildRequestBody(String base64Image) throws Exception {
        JSONObject root = new JSONObject();

        // systemInstruction
        JSONObject systemInstruction = new JSONObject();
        JSONArray sysParts = new JSONArray();
        JSONObject sysPart = new JSONObject();
        sysPart.put("text", SYSTEM_INSTRUCTION);
        sysParts.put(sysPart);
        systemInstruction.put("parts", sysParts);
        root.put("systemInstruction", systemInstruction);

        // contents (image + text)
        JSONArray contents = new JSONArray();
        JSONObject userContent = new JSONObject();
        userContent.put("role", "user");

        JSONArray parts = new JSONArray();

        JSONObject imagePart = new JSONObject();
        JSONObject inlineData = new JSONObject();
        inlineData.put("mimeType", "image/jpeg");
        inlineData.put("data", base64Image);
        imagePart.put("inlineData", inlineData);
        parts.put(imagePart);

        JSONObject textPart = new JSONObject();
        textPart.put("text", USER_PROMPT);
        parts.put(textPart);

        userContent.put("parts", parts);
        contents.put(userContent);
        root.put("contents", contents);

        return root;
    }

    /** Extracts the generated text from a Gemini generateContent JSON response. */
    private String extractTextFromResponse(String jsonResponse) throws Exception {
        JSONObject root = new JSONObject(jsonResponse);
        JSONArray candidates = root.getJSONArray("candidates");
        JSONObject firstCandidate = candidates.getJSONObject(0);
        JSONObject content = firstCandidate.getJSONObject("content");
        JSONArray parts = content.getJSONArray("parts");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length(); i++) {
            JSONObject part = parts.getJSONObject(i);
            if (part.has("text")) {
                sb.append(part.getString("text"));
            }
        }
        return sb.toString();
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
    }

    private void parseRemindersAndSetAlarms(String fullText) {
        progressBar.setVisibility(View.GONE);
        btnAnalyze.setEnabled(true);
        cardResult.setVisibility(View.VISIBLE);

        String displayText = fullText;
        if (fullText != null && fullText.contains("[REMINDERS]")) {
            try {
                int start = fullText.indexOf("[REMINDERS]") + 11;
                int end = fullText.indexOf("[/REMINDERS]");
                if (end > start) {
                    String jsonStr = fullText.substring(start, end).trim();
                    displayText = fullText.substring(0, fullText.indexOf("[REMINDERS]")).trim();

                    JSONObject remindersObj = new JSONObject(jsonStr);
                    JSONArray reminders = remindersObj.getJSONArray("reminders");

                    for (int i = 0; i < reminders.length(); i++) {
                        JSONObject r = reminders.getJSONObject(i);
                        scheduleAlarm(r.getString("med"), r.getString("time"), i);
                    }
                    Toast.makeText(this, "Automatic reminders set!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e(TAG, "Reminder parsing failed", e);
            }
        }
        tvResult.setText(displayText);
    }

    private void scheduleAlarm(String medName, String timeStr, int idOffset) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager != null && !alarmManager.canScheduleExactAlarms()) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
                return;
            }
        }

        try {
            String[] parts = timeStr.split(":");
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);

            if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }

            Intent intent = new Intent(this, AlarmReceiver.class);
            intent.putExtra("med_name", medName);

            int requestId = (int) (System.currentTimeMillis() / 1000) + idOffset;
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestId, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            if (alarmManager != null) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        } catch (Exception e) {
            Log.e(TAG, "Alarm scheduling failed", e);
        }
    }

    private void showError(String message) {
        progressBar.setVisibility(View.GONE);
        btnAnalyze.setEnabled(true);
        tvResult.setText(message);
        cardResult.setVisibility(View.VISIBLE);
    }
}