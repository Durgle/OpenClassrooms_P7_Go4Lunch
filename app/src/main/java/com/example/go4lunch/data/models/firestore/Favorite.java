package com.example.go4lunch.data.models.firestore;

public class Favorite {

    private String uid;
    private String placeId;
    private String userId;

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

}
