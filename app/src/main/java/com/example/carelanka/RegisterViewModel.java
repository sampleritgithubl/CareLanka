package com.example.carelanka;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RegisterViewModel extends ViewModel {

    private final AuthRepository repository;
    private final MutableLiveData<String> registrationStatus = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    @Inject
    public RegisterViewModel(AuthRepository repository) {
        this.repository = repository;
    }

    public LiveData<String> getRegistrationStatus() { return registrationStatus; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }

    public void registerUser(String name, String email, String phone, String password, String role) {
        isLoading.setValue(true);
        repository.getAuth().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = task.getResult().getUser().getUid();
                        User newUser = new User(uid, name, email, phone, role);
                        saveUserToFirestore(uid, newUser);
                    } else {
                        isLoading.setValue(false);
                        registrationStatus.setValue("Error: " + task.getException().getMessage());
                    }
                });
    }

    private void saveUserToFirestore(String uid, User user) {
        repository.getDb().collection("Users").document(uid)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    isLoading.setValue(false);
                    registrationStatus.setValue("SUCCESS");
                })
                .addOnFailureListener(e -> {
                    isLoading.setValue(false);
                    registrationStatus.setValue("Firestore Error: " + e.getMessage());
                });
    }
}
