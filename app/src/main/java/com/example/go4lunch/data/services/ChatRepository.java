package com.example.go4lunch.data.services;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.models.firestore.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * Manage chat message
 */
public class ChatRepository extends FirestoreRepository {

    private static final String COLLECTION_NAME = "chat";
    private static final String USER_ID_FIELD = "userId";
    private static final String MESSAGE_FIELD = "message";
    private static final String DATE_FIELD = "creationDate";

    public ChatRepository(@NonNull FirebaseFirestore firebaseFirestore, @NonNull FirebaseAuth firebaseAuth) {
        super(firebaseFirestore, firebaseAuth);
    }

    /**
     * Get chat collection from firestore
     *
     * @return Favorite collection
     */
    protected CollectionReference getCollection() {
        return firebaseFirestore.collection(COLLECTION_NAME);
    }

    /**
     * Add new message in database
     *
     * @param messageContent Message content
     */
    public void sendMessage(@NonNull String messageContent, long creationTimestamp) {
        String userId = this.getCurrentUserUID();

        if (userId != null) {
            String uid = userId + "_" + creationTimestamp;
            Message message = new Message(uid, messageContent, creationTimestamp, userId);
            this.getCollection().document(uid).set(message)
                    .addOnFailureListener(e -> Log.e("ChatRepository", e.toString()));
        }
    }

    /**
     * Get all messages in the chat
     *
     * @return Message list
     */
    public LiveData<List<Message>> getMessages() {
        MutableLiveData<List<Message>> messages = new MutableLiveData<>();
        this.getCollection()
                .addSnapshotListener((querySnapshots, error) -> {
                    if (error != null) {
                        Log.e("ChatRepository", "getMessages", error);
                    }

                    List<Message> messageList;
                    if (querySnapshots != null) {
                        messageList = querySnapshots.toObjects(Message.class);
                    } else {
                        messageList = new ArrayList<>();
                    }
                    messages.setValue(messageList);
                });
        return messages;
    }

}
