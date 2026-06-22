package com.example.carelanka;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.google.android.gms.tasks.Tasks;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.TranslateRemoteModel;
import java.util.concurrent.ExecutionException;

public class ModelDownloadWorker extends Worker {

    public ModelDownloadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        // RemoteModelManager is obtained via getInstance()
        RemoteModelManager modelManager = RemoteModelManager.getInstance();

        // Use fromLanguageTag for Sinhala if SINHALA constant is missing in this version
        String siLang = TranslateLanguage.fromLanguageTag("si");
        if (siLang == null) {
            return Result.failure();
        }
        
        TranslateRemoteModel siModel = new TranslateRemoteModel.Builder(siLang).build();
        TranslateRemoteModel taModel = new TranslateRemoteModel.Builder(TranslateLanguage.TAMIL).build();

        try {
            // Download the models
            Tasks.await(modelManager.download(siModel, conditions));
            Tasks.await(modelManager.download(taModel, conditions));

            SharedPreferences prefs = getApplicationContext().getSharedPreferences("CareLankaPrefs", Context.MODE_PRIVATE);
            prefs.edit().putBoolean("multilingual_models_ready", true).apply();

            return Result.success();
        } catch (ExecutionException | InterruptedException e) {
            return Result.retry();
        }
    }
}
