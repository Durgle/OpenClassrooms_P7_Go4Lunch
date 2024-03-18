package com.example.go4lunch.data.services;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.models.LunchNotification;
import com.example.go4lunch.data.models.firestore.Place;
import com.example.go4lunch.data.models.firestore.User;
import com.example.go4lunch.utils.SingleLiveEvent;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class UserRepository extends FirestoreRepository {

    private static final String COLLECTION_NAME = "users";
    private static final String DISPLAY_NAME_FIELD = "displayName";
    private static final String EMAIL_FIELD = "email";
    private static final String ID_FIELD = "uid";
    private static final String URL_PICTURE_FIELD = "urlPicture";
    private static final String PLACE_FIELD = "place";
    private static final String PLACE_ID_FIELD = "place.uid";

    public UserRepository(@NonNull FirebaseFirestore firebaseFirestore, @NonNull AuthUI authUI, @NonNull FirebaseAuth firebaseAuth) {
        super(firebaseFirestore, authUI, firebaseAuth);
    }

    protected CollectionReference getCollection() {
        return firebaseFirestore.collection(COLLECTION_NAME);
    }

    public SingleLiveEvent<Boolean> signOut(Context context) {
        SingleLiveEvent<Boolean> disconnected = new SingleLiveEvent<>();
        authUI.signOut(context)
                .addOnSuccessListener(result -> disconnected.setValue(true))
                .addOnFailureListener(e -> {
                    Log.e("UserRepository", "signOut", e);
                    disconnected.setValue(false);
                });
        return disconnected;
    }

    public void createOrUpdateUser() {
        FirebaseUser user = getCurrentUser();
        if (user != null) {

            String urlPicture = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;
            String email = user.getEmail();
            String displayName = user.getDisplayName();
            String uid = user.getUid();

            Task<DocumentSnapshot> userData = this.getCollection().document(uid).get();
            userData.addOnSuccessListener(
                    documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Map<String, Object> updates = new HashMap<>();
                            updates.put(EMAIL_FIELD, email);
                            updates.put(DISPLAY_NAME_FIELD, displayName);
                            updates.put(URL_PICTURE_FIELD, urlPicture);
                            this.getCollection().document(uid).update(updates);
                        } else {
                            User userToCreate = new User(uid, displayName, email, urlPicture);
                            this.getCollection().document(uid).set(userToCreate);
                        }
                    }
            ).addOnFailureListener(e -> Log.e("UserRepository", "createOrUpdateUser", e));
        }
    }

    public void updateChosenRestaurant(Place place) {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            this.getCollection().document(uid).update(PLACE_FIELD, place)
                    .addOnFailureListener(e -> Log.e("UserRepository", "updateChosenRestaurant", e));
        }
    }

    public LiveData<User> getUserData() {
        String uid = this.getCurrentUserUID();
        MutableLiveData<User> userData = new MutableLiveData<>();
        if (uid != null) {

            this.getCollection().document(uid)
                    .addSnapshotListener((documentSnapshot, error) -> {
                        if (error != null) {
                            Log.e("UserRepository", "getUserData", error);
                        }

                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            User user = documentSnapshot.toObject(User.class);
                            userData.setValue(user);
                        } else {
                            userData.setValue(null);
                        }
                    });
        } else {
            userData.setValue(null);
        }

        return userData;
    }

    public LiveData<List<User>> getAllWorkmates() {
        String uid = this.getCurrentUserUID();
        MutableLiveData<List<User>> users = new MutableLiveData<>();
        if (uid != null) {
            this.getCollection().whereNotEqualTo(ID_FIELD, uid)
                    .addSnapshotListener((querySnapshots, error) -> {
                        if (error != null) {
                            Log.e("UserRepository", "getAllWorkmates", error);
                        }

                        List<User> userList = new ArrayList<>();
                        if (querySnapshots != null) {
                            for (QueryDocumentSnapshot document : querySnapshots) {
                                User user = document.toObject(User.class);
                                userList.add(user);
                            }
                        }
                        List<User> sortedUser = userList.stream()
                                .sorted(Comparator.comparing((User user) -> user.getPlace() != null ? 0 : 1)
                                        .thenComparing(User::getDisplayName))
                                .collect(Collectors.toList());
                        users.setValue(sortedUser);
                    });
        } else {
            users.setValue(new ArrayList<>());
        }
        return users;
    }

    public LiveData<List<User>> getAllUsers() {
        MutableLiveData<List<User>> users = new MutableLiveData<>();
        this.getCollection().addSnapshotListener((querySnapshots, error) -> {
                    if (error != null) {
                        Log.e("UserRepository", "getAllUsers", error);
                    }

                    List<User> userList = new ArrayList<>();
                    if (querySnapshots != null) {
                        for (QueryDocumentSnapshot document : querySnapshots) {
                            User user = document.toObject(User.class);
                            userList.add(user);
                        }
                    }
                    users.setValue(userList);
                });
        return users;
    }

    public LiveData<List<User>> getWorkmatesForPlace(@NonNull String placeId) {
        String uid = this.getCurrentUserUID();
        MutableLiveData<List<User>> users = new MutableLiveData<>();
        if (uid != null) {
            this.getCollection()
                    .whereEqualTo(PLACE_ID_FIELD, placeId)
                    .whereNotEqualTo(ID_FIELD, uid)
                    .addSnapshotListener((querySnapshots, error) -> {
                        if (error != null) {
                            Log.e("UserRepository", "getWorkmatesForPlace", error);
                        }

                        List<User> userList = new ArrayList<>();
                        if (querySnapshots != null) {
                            for (QueryDocumentSnapshot document : querySnapshots) {
                                User user = document.toObject(User.class);
                                userList.add(user);
                            }
                        }
                        users.setValue(userList);
                    });
        } else {
            users.setValue(new ArrayList<>());
        }
        return users;
    }

    public LunchNotification getLunchNotificationData() throws ExecutionException, InterruptedException {

        String uid = this.getCurrentUserUID();
        if(uid != null) {
            DocumentSnapshot userDocumentSnapshot = Tasks.await(this.getCollection().document(uid).get());
            if (userDocumentSnapshot != null && userDocumentSnapshot.exists()) {
                User user = userDocumentSnapshot.toObject(User.class);
                if(user != null && user.getPlace() != null){
                    QuerySnapshot workmateQuerySnapshot = Tasks.await(this.getCollection()
                            .whereEqualTo(PLACE_ID_FIELD, user.getPlace().getUid())
                            .whereNotEqualTo(ID_FIELD, user.getUid()).get());

                    LunchNotification notification = new LunchNotification(user,user.getPlace());
                    if (workmateQuerySnapshot != null) {
                        List<User> workmates = new ArrayList<>();
                        for (QueryDocumentSnapshot document : workmateQuerySnapshot) {
                            User workmate = document.toObject(User.class);
                            workmates.add(workmate);
                        }
                        notification.setWorkmate(workmates);
                    }
                    return notification;
                }
            }
        }
        return null;
    }

}
