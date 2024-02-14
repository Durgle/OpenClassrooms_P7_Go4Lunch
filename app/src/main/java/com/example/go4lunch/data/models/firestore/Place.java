package com.example.go4lunch.data.models.firestore;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Place {

    private String uid;
    private String name;

    public Place(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public Place() {}

    // --- GETTERS ---
    public String getUid() {
        return uid;
    }
    public String getName() {
        return name;
    }

    // --- SETTERS ---
    public void setUid(String uid) {
        this.uid = uid;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return Objects.equals(uid, place.uid) && Objects.equals(name, place.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, name);
    }

    @NonNull
    @Override
    public String toString() {
        return "Place{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
