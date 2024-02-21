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

/**
 * Manage favorite
 */
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

    /**
     * Get favorite collection from firestore
     *
     * @return Favorite collection
     */
    private CollectionReference getFavoriteCollection() {
        return firebaseFirestore.collection(COLLECTION_NAME);
    }

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

    /**
     * Add the given place to current user favorite if not exist and remove if the favorite exist
     *
     * @param placeId Place id
     */
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
                        } else {
                            Favorite favorite = new Favorite(uid, placeId , userId);
                            this.getFavoriteCollection().document(uid).set(favorite);
                        }
                    }
            ).addOnFailureListener(e -> Log.e("FavoriteRepository", e.toString()));
        }
    }

    /**
     * Check if the current user has the given place in favorites
     *
     * @param placeId Place Id
     * @return Favorite status live data
     */
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
