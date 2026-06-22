package com.example.carelanka;

import android.util.Log;

import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentifier;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

/**
 * Utility class for Language Identification and Translation using Google ML Kit.
 */
public class LanguageHelper {

    private static final String TAG = "LanguageHelper";

    /**
     * Callback interface for asynchronous operations.
     */
    public interface LanguageCallback {
        void onSuccess(String result);
        void onFailure(Exception e);
    }

    /**
     * Pre-downloads translation models for Sinhala and Tamil to English.
     * Best called during app startup (e.g., in SplashActivity or Dashboard).
     */
    public void downloadModels() {
        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        // Prepare Sinhala to English
        TranslatorOptions siOptions = new TranslatorOptions.Builder()
                .setSourceLanguage("si") // Using ISO 639-1 code for Sinhala
                .setTargetLanguage(TranslateLanguage.ENGLISH)
                .build();
        Translator siTranslator = Translation.getClient(siOptions);
        siTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(unused -> Log.d(TAG, "Sinhala model downloaded."))
                .addOnFailureListener(e -> Log.e(TAG, "Sinhala model download failed", e));

        // Prepare Tamil to English
        TranslatorOptions taOptions = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.TAMIL)
                .setTargetLanguage(TranslateLanguage.ENGLISH)
                .build();
        Translator taTranslator = Translation.getClient(taOptions);
        taTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(unused -> Log.d(TAG, "Tamil model downloaded."))
                .addOnFailureListener(e -> Log.e(TAG, "Tamil model download failed", e));
    }

    /**
     * Detects language and translates to English if it's Sinhala or Tamil.
     *
     * @param text     The input text to process.
     * @param callback Success or failure callback.
     */
    public void translateIfRequired(String text, LanguageCallback callback) {
        LanguageIdentifier languageIdentifier = LanguageIdentification.getClient();

        languageIdentifier.identifyLanguage(text)
                .addOnSuccessListener(languageCode -> {
                    if (languageCode == null || languageCode.equals("und")) {
                        callback.onFailure(new Exception("Could not identify language."));
                    } else if (languageCode.equals("si") || languageCode.equals("ta")) {
                        // Translation needed
                        performTranslation(text, languageCode, callback);
                    } else if (languageCode.equals("en")) {
                        // Already English
                        callback.onSuccess(text);
                    } else {
                        // Other language - return as-is or handle differently
                        callback.onSuccess(text);
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }

    private void performTranslation(String text, String sourceLangCode, LanguageCallback callback) {
        String sourceLanguage;
        if (sourceLangCode.equals("si")) {
            sourceLanguage = "si";
        } else {
            sourceLanguage = TranslateLanguage.TAMIL;
        }

        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(sourceLanguage)
                .setTargetLanguage(TranslateLanguage.ENGLISH)
                .build();

        final Translator translator = Translation.getClient(options);

        DownloadConditions conditions = new DownloadConditions.Builder()
                .build();

        translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(unused -> {
                    translator.translate(text)
                            .addOnSuccessListener(callback::onSuccess)
                            .addOnFailureListener(callback::onFailure)
                            .addOnCompleteListener(task -> translator.close());
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e);
                    translator.close();
                });
    }
}
