package com.example.go4lunch.data.services;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.models.firestore.Setting;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.ExecutionException;

/**
 * Manage setting
 */
public class SettingRepository extends FirestoreRepository {

    private static final String COLLECTION_NAME = "settings";
    private static final String ID_FIELD = "id";
    private static final String VALUE_FIELD = "value";

    public SettingRepository(@NonNull FirebaseFirestore firebaseFirestore, @NonNull AuthUI authUI, @NonNull FirebaseAuth firebaseAuth) {
        super(firebaseFirestore, authUI, firebaseAuth);
    }

    /**
     * Get setting collection from firestore
     *
     * @return Setting collection
     */
    protected CollectionReference getCollection() {
        return firebaseFirestore.collection(COLLECTION_NAME);
    }

    /**
     * Save given settings in database
     *
     * @param enableNotification Enable Notification
     */
    public void saveSetting(boolean enableNotification) {
        String userId = this.getCurrentUserUID();

        if (userId != null) {
            Setting settingObject = new Setting(userId, enableNotification);
            this.getCollection().document(userId).set(settingObject)
                    .addOnFailureListener(e -> Log.e("SettingRepository", e.toString()));
        }
    }

    /**
     * Get setting for the current user
     *
     * @return Settings
     */
    public LiveData<Setting> getSettings() {

        String userId = this.getCurrentUserUID();
        MutableLiveData<Setting> settingsLiveData = new MutableLiveData<>();
        if (userId != null) {
            this.getCollection().document(userId)
                    .addSnapshotListener((documentSnapshot, error) -> {
                        if (error != null) {
                            Log.e("ChatRepository", "getMessages", error);
                        }

                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            Setting settings = documentSnapshot.toObject(Setting.class);
                            settingsLiveData.setValue(settings);
                        } else {
                            settingsLiveData.setValue(null);
                        }
                    });
        }
        return settingsLiveData;
    }

    public Setting getSyncSettings() throws ExecutionException, InterruptedException {

        String uid = this.getCurrentUserUID();
        if (uid != null) {
            DocumentSnapshot settingDocumentSnapshot = Tasks.await(this.getCollection().document(uid).get());
            if (settingDocumentSnapshot != null && settingDocumentSnapshot.exists()) {
                return settingDocumentSnapshot.toObject(Setting.class);
            }
        }
        return null;
    }

}
