package com.example.go4lunch.data.services;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.models.firestore.Favorite;
import com.example.go4lunch.data.models.firestore.Message;
import com.example.go4lunch.data.models.firestore.User;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.type.DateTime;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

/**
 * Manage chat message
 */
public class ChatRepository extends FirestoreRepository{

    private static final String COLLECTION_NAME = "chat";
    private static final String USER_ID_FIELD = "userId";
    private static final String MESSAGE_FIELD = "message";
    private static final String DATE_FIELD = "creationDate";


    public ChatRepository(@NonNull FirebaseFirestore firebaseFirestore, @NonNull AuthUI authUI, @NonNull FirebaseAuth firebaseAuth) {
        super(firebaseFirestore, authUI, firebaseAuth);
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
    public void sendMessage(@NonNull String messageContent) {
        String userId = this.getCurrentUserUID();

        if (userId != null) {
            long date = new Date().getTime();
            String uid = userId + "_" + date;
            Message message = new Message(uid,messageContent,date,userId);
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

                    List<Message> messageList = new ArrayList<>();
                    if (querySnapshots != null) {
                        for (QueryDocumentSnapshot document : querySnapshots) {
                            Message message = document.toObject(Message.class);

                            messageList.add(message);
                        }
                    }
                    messages.setValue(messageList);
                });
            return messages;
    }

}
