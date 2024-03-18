package com.example.go4lunch.data.models.firestore;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Message {

    private String uid;
    private String message;
    private long creationDate;
    private String userId;

    public Message(String uid, String message, long creationDate, String userId) {
        this.uid = uid;
        this.message = message;
        this.creationDate = creationDate;
        this.userId = userId;
    }

    public Message() {
    }

    // --- GETTERS ---
    public String getUid() {
        return uid;
    }

    public String getMessage() {
        return message;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public String getUserId() {
        return userId;
    }

    // --- SETTERS ---
    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCreationDate(long dateCreated) {
        this.creationDate = dateCreated;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return creationDate == message1.creationDate && Objects.equals(uid, message1.uid) && Objects.equals(message, message1.message) && Objects.equals(userId, message1.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, message, creationDate, userId);
    }

    @NonNull
    @Override
    public String toString() {
        return "Message{" +
                "uid='" + uid + '\'' +
                ", message='" + message + '\'' +
                ", creationDate=" + creationDate +
                ", userId='" + userId + '\'' +
                '}';
    }
}
