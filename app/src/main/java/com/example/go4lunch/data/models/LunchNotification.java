package com.example.go4lunch.data.models;

import androidx.annotation.NonNull;

import com.example.go4lunch.data.models.firestore.Place;
import com.example.go4lunch.data.models.firestore.User;

import java.util.List;
import java.util.Objects;

public class LunchNotification {

    private final User user;

    private final Place place;

    private List<User> workmate = null;

    public LunchNotification(@NonNull User user, @NonNull Place place) {
        this.user = user;
        this.place = place;
    }

    public User getUser() {
        return user;
    }

    public Place getPlace() {
        return place;
    }

    public List<User> getWorkmate() {
        return workmate;
    }

    public void setWorkmate(List<User> workmate) {
        this.workmate = workmate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LunchNotification that = (LunchNotification) o;
        return Objects.equals(user, that.user) && Objects.equals(place, that.place) && Objects.equals(workmate, that.workmate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, place, workmate);
    }

    @NonNull
    @Override
    public String toString() {
        return "LunchNotification{" +
                "user=" + user +
                ", place=" + place +
                ", workmate=" + workmate +
                '}';
    }
}
