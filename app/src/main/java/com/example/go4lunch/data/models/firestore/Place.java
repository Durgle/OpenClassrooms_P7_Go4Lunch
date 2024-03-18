package com.example.go4lunch.data.models.firestore;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Place {

    private String uid;
    private String name;
    private String address;

    public Place(String uid, String name, String address) {
        this.uid = uid;
        this.name = name;
        this.address = address;
    }

    public Place() {
    }

    // --- GETTERS ---
    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    // --- SETTERS ---
    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return Objects.equals(uid, place.uid) && Objects.equals(name, place.name) && Objects.equals(address, place.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, name, address);
    }

    @NonNull
    @Override
    public String toString() {
        return "Place{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
