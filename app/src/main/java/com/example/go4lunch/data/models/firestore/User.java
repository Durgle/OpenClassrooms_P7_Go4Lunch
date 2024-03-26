package com.example.go4lunch.data.models.firestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class User {

    private String uid;
    private String displayName;
    private String email;
    @Nullable
    private String urlPicture;
    private Place place;

    public User(String uid, String displayName, String email, @Nullable String urlPicture) {
        this.uid = uid;
        this.displayName = displayName;
        this.email = email;
        this.urlPicture = urlPicture;
    }

    public User(String uid, String displayName, String email, @Nullable String urlPicture, @Nullable Place place) {
        this.uid = uid;
        this.displayName = displayName;
        this.email = email;
        this.urlPicture = urlPicture;
        this.place = place;
    }

    public User() {}

    // --- GETTERS ---
    public String getUid() {
        return uid;
    }
    public String getDisplayName() {
        return displayName;
    }
    public String getEmail() {
        return email;
    }
    @Nullable
    public String getUrlPicture() {
        return urlPicture;
    }
    @Nullable
    public Place getPlace() {
        return place;
    }

    // --- SETTERS ---
    public void setUid(String uid) {
        this.uid = uid;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setUrlPicture(@Nullable String urlPicture) {
        this.urlPicture = urlPicture;
    }
    public void setPlace(Place place) {
        this.place = place;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(uid, user.uid) && Objects.equals(displayName, user.displayName) && Objects.equals(email, user.email) && Objects.equals(urlPicture, user.urlPicture) && Objects.equals(place, user.place);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, displayName, email, urlPicture, place);
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", displayName='" + displayName + '\'' +
                ", email='" + email + '\'' +
                ", urlPicture='" + urlPicture + '\'' +
                ", place=" + place +
                '}';
    }
}
