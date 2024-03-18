package com.example.go4lunch.ui.chat;

import androidx.annotation.NonNull;

import com.example.go4lunch.data.models.firestore.User;

import java.util.Objects;

public class MessageViewState {

    private final String id;
    private final User user;
    private final String message;
    private final String date;
    private final boolean isCurrentUser;

    public MessageViewState(@NonNull String id, @NonNull User user, @NonNull String message, @NonNull String date, boolean isCurrentUser) {
        this.id = id;
        this.user = user;
        this.message = message;
        this.date = date;
        this.isCurrentUser = isCurrentUser;
    }

    public String getId() {
        return id;
    }

    @NonNull
    public User getUser() {
        return user;
    }

    @NonNull
    public String getMessage() {
        return message;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    public boolean isCurrentUser() {
        return isCurrentUser;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageViewState that = (MessageViewState) o;
        return isCurrentUser == that.isCurrentUser && Objects.equals(id, that.id) && Objects.equals(user, that.user) && Objects.equals(message, that.message) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, message, date, isCurrentUser);
    }

    @NonNull
    @Override
    public String toString() {
        return "MessageViewState{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", message='" + message + '\'' +
                ", date='" + date + '\'' +
                ", isCurrentUser=" + isCurrentUser +
                '}';
    }
}
