package com.example.go4lunch.data.services;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.models.firestore.Favorite;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.annotation.Nullable;

public class FavoriteRepository {

    private static final String COLLECTION_NAME = "favorites";
    private static final String USER_ID_FIELD = "userId";
    private static final String PLACE_ID_FIELD = "placeId";
    private static final String VALUE_FIELD = "value";

    @NonNull
    private final FirebaseFirestore firebaseFirestore;
    @NonNull
    private final AuthUI authUI;
    @NonNull
    private final FirebaseAuth firebaseAuth;

    public FavoriteRepository(@NonNull FirebaseFirestore firebaseFirestore, @NonNull AuthUI authUI, @NonNull FirebaseAuth firebaseAuth) {
        this.firebaseFirestore = firebaseFirestore;
        this.authUI = authUI;
        this.firebaseAuth = firebaseAuth;
    }

    private CollectionReference getFavoriteCollection() {
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

    public void togglePlaceLike(@NonNull String placeId) {
        String userId = this.getCurrentUserUID();

        if (userId != null) {
            String uid = userId + "_" + placeId;
            Task<DocumentSnapshot> favoritePlace = this.getFavoriteCollection().document(uid).get();
            favoritePlace.addOnSuccessListener(
                    documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Boolean currentValue = documentSnapshot.getBoolean(VALUE_FIELD);
                            this.getFavoriteCollection().document(uid).delete();
                            //this.getFavoriteCollection().document(uid).update(VALUE_FIELD, currentValue == null || !currentValue);
                        } else {
                            Favorite favorite = new Favorite(uid, placeId , userId);
                            this.getFavoriteCollection().document(uid).set(favorite);
                        }
                    }
            ).addOnFailureListener(e -> Log.e("FavoriteRepository", e.toString()));
        }
    }

    public LiveData<Boolean> isLike(@NonNull String placeId) {
        String userId = this.getCurrentUserUID();
        MutableLiveData<Boolean> isLike = new MutableLiveData<>();

        String uid = userId + "_" + placeId;
        if (userId != null) {
            this.getFavoriteCollection().document(uid)
                    .addSnapshotListener((documentSnapshot, error) -> {
                        if (error != null) {
                            Log.e("FavoriteRepository", error.toString());
                        }

                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            isLike.setValue(true);
                        } else {
                            isLike.setValue(false);
                        }
                    });
        } else {
            isLike.setValue(null);
        }
        return isLike;
    }

}
