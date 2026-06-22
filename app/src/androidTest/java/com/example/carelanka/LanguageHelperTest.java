package com.example.carelanka;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentifier;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@RunWith(AndroidJUnit4.class)
public class LanguageHelperTest {

    private LanguageHelper languageHelper;

    @Mock
    private LanguageIdentifier mockLanguageIdentifier;
    @Mock
    private Translator mockTranslator;
    @Mock
    private LanguageHelper.LanguageCallback mockCallback;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        languageHelper = new LanguageHelper();
    }

    @Test
    public void testSinhalaTranslation() {
        String input = "මට හිසරදයක් ඇත";
        String expectedTranslation = "I have a headache";

        try (MockedStatic<LanguageIdentification> langIdStatic = Mockito.mockStatic(LanguageIdentification.class);
             MockedStatic<Translation> translationStatic = Mockito.mockStatic(Translation.class)) {

            langIdStatic.when(LanguageIdentification::getClient).thenReturn(mockLanguageIdentifier);
            translationStatic.when(() -> Translation.getClient(any())).thenReturn(mockTranslator);

            // Mock Language Identification
            Task<String> mockIdTask = mock(Task.class);
            when(mockLanguageIdentifier.identifyLanguage(input)).thenReturn(mockIdTask);
            when(mockIdTask.addOnSuccessListener(any())).thenAnswer(invocation -> {
                OnSuccessListener<String> listener = invocation.getArgument(0);
                listener.onSuccess("si");
                return mockIdTask;
            });

            // Mock Model Download
            Task<Void> mockDownloadTask = mock(Task.class);
            when(mockTranslator.downloadModelIfNeeded(any())).thenReturn(mockDownloadTask);
            when(mockDownloadTask.addOnSuccessListener(any())).thenAnswer(invocation -> {
                OnSuccessListener<Void> listener = invocation.getArgument(0);
                listener.onSuccess(null);
                return mockDownloadTask;
            });

            // Mock Translation
            Task<String> mockTranslateTask = mock(Task.class);
            when(mockTranslator.translate(input)).thenReturn(mockTranslateTask);
            when(mockTranslateTask.addOnSuccessListener(any())).thenAnswer(invocation -> {
                OnSuccessListener<String> listener = invocation.getArgument(0);
                listener.onSuccess(expectedTranslation);
                return mockTranslateTask;
            });
            when(mockTranslateTask.addOnFailureListener(any())).thenReturn(mockTranslateTask);
            when(mockTranslateTask.addOnCompleteListener(any())).thenReturn(mockTranslateTask);

            languageHelper.translateIfRequired(input, mockCallback);

            verify(mockCallback).onSuccess(expectedTranslation);
        }
    }

    @Test
    public void testTamilTranslation() {
        String input = "என் தலை வலிக்கிறது";
        String expectedTranslation = "My head hurts";

        try (MockedStatic<LanguageIdentification> langIdStatic = Mockito.mockStatic(LanguageIdentification.class);
             MockedStatic<Translation> translationStatic = Mockito.mockStatic(Translation.class)) {

            langIdStatic.when(LanguageIdentification::getClient).thenReturn(mockLanguageIdentifier);
            translationStatic.when(() -> Translation.getClient(any())).thenReturn(mockTranslator);

            Task<String> mockIdTask = mock(Task.class);
            when(mockLanguageIdentifier.identifyLanguage(input)).thenReturn(mockIdTask);
            when(mockIdTask.addOnSuccessListener(any())).thenAnswer(invocation -> {
                ((OnSuccessListener<String>) invocation.getArgument(0)).onSuccess("ta");
                return mockIdTask;
            });

            Task<Void> mockDownloadTask = mock(Task.class);
            when(mockTranslator.downloadModelIfNeeded(any())).thenReturn(mockDownloadTask);
            when(mockDownloadTask.addOnSuccessListener(any())).thenAnswer(invocation -> {
                ((OnSuccessListener<Void>) invocation.getArgument(0)).onSuccess(null);
                return mockDownloadTask;
            });

            Task<String> mockTranslateTask = mock(Task.class);
            when(mockTranslator.translate(input)).thenReturn(mockTranslateTask);
            when(mockTranslateTask.addOnSuccessListener(any())).thenAnswer(invocation -> {
                ((OnSuccessListener<String>) invocation.getArgument(0)).onSuccess(expectedTranslation);
                return mockTranslateTask;
            });
            when(mockTranslateTask.addOnFailureListener(any())).thenReturn(mockTranslateTask);
            when(mockTranslateTask.addOnCompleteListener(any())).thenReturn(mockTranslateTask);

            languageHelper.translateIfRequired(input, mockCallback);

            verify(mockCallback).onSuccess(expectedTranslation);
        }
    }

    @Test
    public void testEnglishNoTranslation() {
        String input = "I have a headache";

        try (MockedStatic<LanguageIdentification> langIdStatic = Mockito.mockStatic(LanguageIdentification.class)) {
            langIdStatic.when(LanguageIdentification::getClient).thenReturn(mockLanguageIdentifier);

            Task<String> mockIdTask = mock(Task.class);
            when(mockLanguageIdentifier.identifyLanguage(input)).thenReturn(mockIdTask);
            when(mockIdTask.addOnSuccessListener(any())).thenAnswer(invocation -> {
                ((OnSuccessListener<String>) invocation.getArgument(0)).onSuccess("en");
                return mockIdTask;
            });

            languageHelper.translateIfRequired(input, mockCallback);

            verify(mockCallback).onSuccess(input);
        }
    }

    @Test
    public void testEmptyInputFailure() {
        String input = "";

        try (MockedStatic<LanguageIdentification> langIdStatic = Mockito.mockStatic(LanguageIdentification.class)) {
            langIdStatic.when(LanguageIdentification::getClient).thenReturn(mockLanguageIdentifier);

            Task<String> mockIdTask = mock(Task.class);
            when(mockLanguageIdentifier.identifyLanguage(input)).thenReturn(mockIdTask);
            when(mockIdTask.addOnSuccessListener(any())).thenAnswer(invocation -> {
                ((OnSuccessListener<String>) invocation.getArgument(0)).onSuccess("und");
                return mockIdTask;
            });

            languageHelper.translateIfRequired(input, mockCallback);

            verify(mockCallback).onFailure(any(Exception.class));
        }
    }

    @Test
    public void testNetworkFailure() {
        String input = "මට හිසරදයක් ඇත";

        try (MockedStatic<LanguageIdentification> langIdStatic = Mockito.mockStatic(LanguageIdentification.class);
             MockedStatic<Translation> translationStatic = Mockito.mockStatic(Translation.class)) {

            langIdStatic.when(LanguageIdentification::getClient).thenReturn(mockLanguageIdentifier);
            translationStatic.when(() -> Translation.getClient(any())).thenReturn(mockTranslator);

            Task<String> mockIdTask = mock(Task.class);
            when(mockLanguageIdentifier.identifyLanguage(input)).thenReturn(mockIdTask);
            when(mockIdTask.addOnSuccessListener(any())).thenAnswer(invocation -> {
                ((OnSuccessListener<String>) invocation.getArgument(0)).onSuccess("si");
                return mockIdTask;
            });

            Task<Void> mockDownloadTask = mock(Task.class);
            when(mockTranslator.downloadModelIfNeeded(any())).thenReturn(mockDownloadTask);
            when(mockDownloadTask.addOnFailureListener(any())).thenAnswer(invocation -> {
                ((OnFailureListener) invocation.getArgument(0)).onFailure(new Exception("Network Error"));
                return mockDownloadTask;
            });

            languageHelper.translateIfRequired(input, mockCallback);

            verify(mockCallback).onFailure(any(Exception.class));
        }
    }
}
