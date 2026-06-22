package com.example.carelanka;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentifier;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MultilingualAIActivity extends AppCompatActivity {

    private static final String TAG = "MultilingualAI";

    // ✅ Same key, BuildConfig හරහා — code එකේ hardcode කරලා නෑ
    private static final String API_KEY = BuildConfig.GEMINI_API_KEY;
    private static final String MODEL_NAME = "gemini-2.5-flash";
    private static final String GEMINI_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/"
                    + MODEL_NAME + ":generateContent?key=" + API_KEY;

    private static final String SYSTEM_INSTRUCTION =
            "You are CareLanka AI, a professional and friendly medical assistant for Sri Lankans. " +
                    "You can discuss health, wellness, and general topics. " +
                    "Always be empathetic and use clear language. Support English, Sinhala, and Tamil. " +
                    "Crucially: If symptoms sound serious, strongly advise the user to see a doctor or call 1990 (Suwa Seriya).";

    private RecyclerView rvChat;
    private ChatAdapter adapter;
    private final List<ChatMessage> messages = new ArrayList<>();
    private EditText etMessage;
    private FloatingActionButton btnSend;
    private ProgressBar progressBar;
    private TextView tvLanguageIndicator;

    // Manually tracked chat history — REST API එකේ "contents" array එකට යවන්න
    private final JSONArray chatHistory = new JSONArray();

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();

    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multilingual_ai);

        rvChat = findViewById(R.id.rvChat);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        progressBar = findViewById(R.id.progressBar);
        tvLanguageIndicator = findViewById(R.id.tvLanguageIndicator);

        adapter = new ChatAdapter(messages);
        rvChat.setLayoutManager(new LinearLayoutManager(this));
        rvChat.setAdapter(adapter);

        btnSend.setOnClickListener(v -> sendMessage());

        // Initial Greeting
        addMessage(new ChatMessage(
                "Hello! I am CareLanka AI. How can I help you today? (ඔබට උදව් කරන්නේ කෙසේද? / நான் ඔබට எப்படி உதவ முடியும்?)",
                false,
                currentTimestamp()
        ));
    }

    private void sendMessage() {
        String input = etMessage.getText().toString().trim();
        if (input.isEmpty()) return;

        if (API_KEY == null || API_KEY.isEmpty()) {
            Toast.makeText(this, "API key not configured (local.properties)", Toast.LENGTH_LONG).show();
            return;
        }

        addMessage(new ChatMessage(input, true, currentTimestamp()));
        etMessage.setText("");
        showLoading(true);

        detectLanguageCode(input, languageCode -> {
            updateLanguageLabel(languageCode);
            // Gemini already understands Sinhala/Tamil/English directly,
            // so we send the original text — no need to pre-translate to English.
            callGeminiChat(input, languageCode);
        });
    }

    private void detectLanguageCode(String text, OnLanguageDetectedListener listener) {
        LanguageIdentifier identifier = LanguageIdentification.getClient();
        identifier.identifyLanguage(text)
                .addOnSuccessListener(listener::onDetected)
                .addOnFailureListener(e -> listener.onDetected("und"));
    }

    private void updateLanguageLabel(String code) {
        String label = "Language: " + code;
        if ("si".equals(code)) label = "සිංහල (Sinhala)";
        else if ("ta".equals(code)) label = "தமிழ் (Tamil)";
        else if ("en".equals(code)) label = "English";

        tvLanguageIndicator.setText(label);
        tvLanguageIndicator.setVisibility(View.VISIBLE);
    }

    private void callGeminiChat(String userText, String originalLangCode) {
        try {
            // Add user turn to history
            chatHistory.put(buildTurn("user", userText));

            JSONObject requestBody = buildRequestBody();

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
                        mainHandler.post(() -> showError("API Error (" + response.code() + ")"));
                        return;
                    }

                    try {
                        String aiText = extractTextFromResponse(responseBody);
                        // Add model turn to history so context carries forward
                        chatHistory.put(buildTurn("model", aiText));
                        mainHandler.post(() -> displayAiMessage(aiText));
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

    private JSONObject buildTurn(String role, String text) throws Exception {
        JSONObject turn = new JSONObject();
        turn.put("role", role);
        JSONArray parts = new JSONArray();
        JSONObject part = new JSONObject();
        part.put("text", text);
        parts.put(part);
        turn.put("parts", parts);
        return turn;
    }

    private JSONObject buildRequestBody() throws Exception {
        JSONObject root = new JSONObject();

        JSONObject systemInstruction = new JSONObject();
        JSONArray sysParts = new JSONArray();
        JSONObject sysPart = new JSONObject();
        sysPart.put("text", SYSTEM_INSTRUCTION);
        sysParts.put(sysPart);
        systemInstruction.put("parts", sysParts);
        root.put("systemInstruction", systemInstruction);

        // Full conversation history each time — Gemini REST API is stateless
        root.put("contents", chatHistory);

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

    /**
     * Optionally translates the AI's English-ish reply into the user's detected
     * language using ML Kit on-device translation. Only needed if you want a
     * hard guarantee of native-script output; Gemini usually already replies
     * in the same language it was asked in.
     */
    private void translateAndDisplay(String text, String targetLangCode) {
        if ("en".equals(targetLangCode) || "und".equals(targetLangCode)) {
            displayAiMessage(text);
            return;
        }

        String mlKitCode = "si".equals(targetLangCode) ? "si" : TranslateLanguage.TAMIL;

        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(mlKitCode)
                .build();

        final Translator translator = Translation.getClient(options);
        translator.downloadModelIfNeeded()
                .addOnSuccessListener(unused -> {
                    translator.translate(text)
                            .addOnSuccessListener(translated -> {
                                displayAiMessage(translated);
                                translator.close();
                            })
                            .addOnFailureListener(e -> {
                                displayAiMessage(text);
                                translator.close();
                            });
                })
                .addOnFailureListener(e -> {
                    displayAiMessage(text);
                    translator.close();
                });
    }

    private void displayAiMessage(String text) {
        addMessage(new ChatMessage(text, false, currentTimestamp()));
        showLoading(false);
    }

    private void addMessage(ChatMessage message) {
        messages.add(message);
        adapter.notifyItemInserted(messages.size() - 1);
        rvChat.scrollToPosition(messages.size() - 1);
    }

    /** Shows/hides a loading bubble using the adapter's TYPE_LOADING item. */
    private void showLoading(boolean loading) {
        btnSend.setEnabled(!loading);

        boolean lastIsLoading = !messages.isEmpty() && messages.get(messages.size() - 1).isLoading();

        if (loading && !lastIsLoading) {
            messages.add(new ChatMessage(true));
            adapter.notifyItemInserted(messages.size() - 1);
            rvChat.scrollToPosition(messages.size() - 1);
        } else if (!loading && lastIsLoading) {
            int index = messages.size() - 1;
            messages.remove(index);
            adapter.notifyItemRemoved(index);
        }
    }

    private void showError(String error) {
        showLoading(false);
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    private String currentTimestamp() {
        return DateFormat.format("hh:mm a", System.currentTimeMillis()).toString();
    }

    interface OnLanguageDetectedListener {
        void onDetected(String code);
    }
}