package com.example.go4lunch.data.services;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.models.firestore.Place;
import com.example.go4lunch.data.models.firestore.User;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class UserRepository {

    private static final String COLLECTION_NAME = "users";
    private static final String DISPLAY_NAME_FIELD = "displayName";
    private static final String EMAIL_FIELD = "email";
    private static final String ID_FIELD = "uid";
    private static final String URL_PICTURE_FIELD = "urlPicture";
    private static final String PLACE_FIELD = "place";
    private static final String PLACE_ID_FIELD = "place.uid";

    @NonNull
    private final FirebaseFirestore firebaseFirestore;
    @NonNull
    private final AuthUI authUI;
    @NonNull
    private final FirebaseAuth firebaseAuth;

    private final MutableLiveData<User> currentUser = new MutableLiveData<>();
    private final MutableLiveData<Boolean> logged = new MutableLiveData<>();
    private final MutableLiveData<List<User>> userList = new MutableLiveData<>();
    private final MutableLiveData<List<User>> placeUserList = new MutableLiveData<>();

    public UserRepository(@NonNull FirebaseFirestore firebaseFirestore, @NonNull AuthUI authUI, @NonNull FirebaseAuth firebaseAuth) {
        this.firebaseFirestore = firebaseFirestore;
        this.authUI = authUI;
        this.firebaseAuth = firebaseAuth;
    }

    private CollectionReference getUsersCollection(){
        return firebaseFirestore.collection(COLLECTION_NAME);
    }

    @Nullable
    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    public String getCurrentUserUID() {
        FirebaseUser user = getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }

    public void signOut(Context context) {
        authUI.signOut(context)
                .addOnSuccessListener( result -> logged.setValue(false))
                .addOnFailureListener(e -> Log.e("UserRepository",e.toString()));
    }

    public LiveData<Boolean> isLogged() {
        return logged;
    }

    public void createOrUpdateUser() {
        FirebaseUser user = getCurrentUser();
        if(user != null){
            logged.setValue(true);

            String urlPicture = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;
            String email = user.getEmail();
            String displayName = user.getDisplayName();
            String uid = user.getUid();

            Task<DocumentSnapshot> userData =  this.getUsersCollection().document(uid).get();
            userData.addOnSuccessListener(
                    documentSnapshot -> {
                        if(documentSnapshot.exists()){
                            Map<String, Object> updates = new HashMap<>();
                            updates.put(EMAIL_FIELD, email);
                            updates.put(DISPLAY_NAME_FIELD, displayName);
                            updates.put(URL_PICTURE_FIELD, urlPicture);
                            this.getUsersCollection().document(uid).update(updates)
                                    .addOnSuccessListener(result -> getUserData());
                        } else {
                            User userToCreate = new User(uid, displayName, email,urlPicture);
                            this.getUsersCollection().document(uid).set(userToCreate)
                                    .addOnSuccessListener(result -> getUserData());
                        }
                    }
            ).addOnFailureListener(e -> Log.e("UserRepository",e.toString()));
        }
    }

    public LiveData<User> getUserData(){
        String uid = this.getCurrentUserUID();
        if(uid != null){
            Task<DocumentSnapshot> userData =  this.getUsersCollection().document(uid).get();
            userData.continueWith(task -> task.getResult().toObject(User.class))
                    .addOnSuccessListener(currentUser::setValue)
                    .addOnFailureListener(e -> Log.e("UserRepository", e.toString()));
        } else {
            currentUser.setValue(null);
        }

        return currentUser;
    }

    public LiveData<List<User>> getAllUser(){
        String uid = this.getCurrentUserUID();
        if(uid != null){
            this.getUsersCollection().whereNotEqualTo(ID_FIELD, uid).get()
                    .addOnSuccessListener(querySnapshots -> {
                        List<User> userList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : querySnapshots) {
                            User user = document.toObject(User.class);
                            userList.add(user);
                        }
                        this.userList.setValue(userList);
                    })
                    .addOnFailureListener(e -> Log.e("UserRepository", e.toString()));
        }else{
            this.userList.setValue(new ArrayList<>());
        }
        return this.userList;
    }

    public LiveData<List<User>> getUserForPlace(@NonNull String placeId){
        String uid = this.getCurrentUserUID();
        if(uid != null){
            this.getUsersCollection().whereEqualTo(PLACE_ID_FIELD, placeId).get()
                    .addOnSuccessListener(querySnapshots -> {
                        List<User> userList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : querySnapshots) {
                            User user = document.toObject(User.class);
                            userList.add(user);
                        }
                        this.placeUserList.setValue(userList);
                    })
                    .addOnFailureListener(e -> Log.e("UserRepository", e.toString()));
        }else{
            this.placeUserList.setValue(new ArrayList<>());
        }
        return this.placeUserList;
    }

}
