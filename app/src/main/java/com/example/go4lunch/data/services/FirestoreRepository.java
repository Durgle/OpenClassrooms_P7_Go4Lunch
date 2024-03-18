package com.example.go4lunch.data.services;


import androidx.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.annotation.Nullable;

public abstract class FirestoreRepository {

    @NonNull
    protected final FirebaseFirestore firebaseFirestore;
    @NonNull
    protected final AuthUI authUI;
    @NonNull
    protected final FirebaseAuth firebaseAuth;

    public FirestoreRepository(@NonNull FirebaseFirestore firebaseFirestore, @NonNull AuthUI authUI, @NonNull FirebaseAuth firebaseAuth) {
        this.firebaseFirestore = firebaseFirestore;
        this.authUI = authUI;
        this.firebaseAuth = firebaseAuth;
    }

    abstract CollectionReference getCollection();

    /**
     * Get current user authenticated
     *
     * @return Current user
     */
    @Nullable
    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    /**
     * Get current user uid
     *
     * @return User uid
     */
    public String getCurrentUserUID() {
        FirebaseUser user = getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }

}
