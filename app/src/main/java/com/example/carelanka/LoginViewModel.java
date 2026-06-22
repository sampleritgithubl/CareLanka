package com.example.carelanka;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LoginViewModel extends ViewModel {

    private final AuthRepository repository;
    private final MutableLiveData<String> loginError = new MutableLiveData<>();
    private final MutableLiveData<String> userRole = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    @Inject
    public LoginViewModel(AuthRepository repository) {
        this.repository = repository;
    }

    public LiveData<String> getLoginError() { return loginError; }
    public LiveData<String> getUserRole() { return userRole; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }

    public void login(String email, String password) {
        isLoading.setValue(true);
        repository.getAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        fetchUserRole(task.getResult().getUser().getUid());
                    } else {
                        isLoading.setValue(false);
                        loginError.setValue(task.getException().getMessage());
                    }
                });
    }

    private void fetchUserRole(String uid) {
        repository.getDb().collection("Users").document(uid).get()
                .addOnCompleteListener(task -> {
                    isLoading.setValue(false);
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            userRole.setValue(document.getString("role"));
                        } else {
                            loginError.setValue("User details not found");
                        }
                    } else {
                        loginError.setValue(task.getException().getMessage());
                    }
                });
    }
}
