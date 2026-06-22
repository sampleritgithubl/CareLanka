package com.example.carelanka;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.lifecycle.LiveData;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

public class ModelDownloadManager {
    private static final String PREFS_NAME = "CareLankaPrefs";
    private static final String KEY_MODELS_READY = "multilingual_models_ready";
    private final Context context;

    public ModelDownloadManager(Context context) {
        this.context = context;
    }

    public boolean areModelsReady() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_MODELS_READY, false);
    }

    public LiveData<WorkInfo> downloadModels() {
        WorkManager workManager = WorkManager.getInstance(context);
        OneTimeWorkRequest downloadRequest = new OneTimeWorkRequest.Builder(ModelDownloadWorker.class)
                .addTag("model_download")
                .build();
        
        workManager.enqueue(downloadRequest);
        return workManager.getWorkInfoByIdLiveData(downloadRequest.getId());
    }
}
