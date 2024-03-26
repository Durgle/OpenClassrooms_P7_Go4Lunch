package com.example.go4lunch.data.models.firestore;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Favorite {

    private String uid;
    private String placeId;
    private String userId;

    /**
     * Favorite constructor
     *
     * @param uid Uid
     * @param placeId Place Id
     * @param userId User Id
     */
    public Favorite(String uid, String placeId, String userId) {
        this.uid = uid;
        this.placeId = placeId;
        this.userId = userId;
    }

    public Favorite() {}

    // --- GETTERS ---
    public String getUid() {
        return uid;
    }
    public String getPlaceId() {
        return placeId;
    }
    public String getUserId() {
        return userId;
    }

    // --- SETTERS ---
    public void setUid(String uid) {
        this.uid = uid;
    }
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favorite favorite = (Favorite) o;
        return Objects.equals(uid, favorite.uid) && Objects.equals(placeId, favorite.placeId) && Objects.equals(userId, favorite.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, placeId, userId);
    }

    @NonNull
    @Override
    public String toString() {
        return "Favorite{" +
                "uid='" + uid + '\'' +
                ", placeId='" + placeId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
