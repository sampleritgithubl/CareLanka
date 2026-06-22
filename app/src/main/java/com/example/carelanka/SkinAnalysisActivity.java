package com.example.carelanka;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
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
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SkinAnalysisActivity extends AppCompatActivity {

    private static final String TAG = "SkinAnalysis";

    // ✅ BuildConfig එකෙන් — code එකේ hardcode කරලා නෑ
    private static final String API_KEY = BuildConfig.GEMINI_API_KEY;
    private static final String MODEL_NAME = "gemini-2.5-flash";
    private static final String GEMINI_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/"
                    + MODEL_NAME + ":generateContent?key=" + API_KEY;

    private static final String SYSTEM_INSTRUCTION =
            "You are a dermatology assistant AI. " +
                    "Analyze the provided skin image and describe any visible conditions or concerns in simple terms. " +
                    "Crucially: Always state that this is NOT a medical diagnosis. " +
                    "Strongly advise the user to see a professional dermatologist for an accurate evaluation. " +
                    "If symptoms look severe (like spreading infection), suggest immediate medical consultation.";

    private static final String USER_PROMPT =
            "Please analyze this skin image and provide a preliminary observation with medical disclaimers.";

    private ImageView ivSkinImage;
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
        setContentView(R.layout.activity_skin_analysis);

        ivSkinImage = findViewById(R.id.ivSkinImage);
        tvResult = findViewById(R.id.tvResult);
        progressBar = findViewById(R.id.progressBar);
        cardResult = findViewById(R.id.cardResult);
        btnAnalyze = findViewById(R.id.btnAnalyze);
        Button btnPickImage = findViewById(R.id.btnPickImage);

        Toolbar toolbar = findViewById(R.id.toolbar);
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
                            ivSkinImage.setImageBitmap(selectedBitmap);
                            ivSkinImage.setAlpha(1.0f);
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

        btnAnalyze.setOnClickListener(v -> analyzeSkin());
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

    private void analyzeSkin() {
        if (selectedBitmap == null) return;

        if (API_KEY == null || API_KEY.isEmpty()) {
            showError("API key not configured (local.properties)");
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
                        mainHandler.post(() -> showError("API Error (" + response.code() + "). Check Logcat."));
                        return;
                    }

                    try {
                        String text = extractTextFromResponse(responseBody);
                        mainHandler.post(() -> showResult(text));
                    } catch (Exception e) {
                        Log.e(TAG, "Response parsing failed", e);
                        mainHandler.post(() -> showError("Response parsing failed"));
                    }
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Request build failed", e);
            showError("Request build failed: " + e.getMessage());
        }
    }

    private JSONObject buildRequestBody(String base64Image) throws Exception {
        JSONObject root = new JSONObject();

        JSONObject systemInstruction = new JSONObject();
        JSONArray sysParts = new JSONArray();
        JSONObject sysPart = new JSONObject();
        sysPart.put("text", SYSTEM_INSTRUCTION);
        sysParts.put(sysPart);
        systemInstruction.put("parts", sysParts);
        root.put("systemInstruction", systemInstruction);

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

    private void showResult(String text) {
        progressBar.setVisibility(View.GONE);
        btnAnalyze.setEnabled(true);
        cardResult.setVisibility(View.VISIBLE);
        tvResult.setText(text);
    }

    private void showError(String message) {
        progressBar.setVisibility(View.GONE);
        btnAnalyze.setEnabled(true);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}