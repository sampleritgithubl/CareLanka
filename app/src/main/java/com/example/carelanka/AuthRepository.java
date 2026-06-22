package com.example.carelanka;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AuthRepository {
    private final FirebaseAuth mAuth;
    private final FirebaseFirestore db;

    @Inject
    public AuthRepository() {
        this.mAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
    }

    public FirebaseAuth getAuth() {
        return mAuth;
    }

    public FirebaseFirestore getDb() {
        return db;
    }
}
