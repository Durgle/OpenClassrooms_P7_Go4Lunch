package com.example.go4lunch.ui.placeDetail.viewState;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class PlaceDetailViewState {

    @NonNull
    private final UserState user;
    @NonNull
    private final PlaceState place;
    @NonNull
    private final List<WorkmateState> workmates;

    public PlaceDetailViewState(
            @NonNull UserState user,
            @NonNull PlaceState place,
            @NonNull List<WorkmateState> workmates
    ) {
        this.user = user;
        this.place = place;
        this.workmates = workmates;
    }

    @NonNull
    public UserState getUser() {
        return user;
    }

    @NonNull
    public PlaceState getPlace() {
        return place;
    }

    @NonNull
    public List<WorkmateState> getWorkmates() {
        return workmates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceDetailViewState viewState = (PlaceDetailViewState) o;
        return Objects.equals(user, viewState.user) && Objects.equals(place, viewState.place) && Objects.equals(workmates, viewState.workmates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, place, workmates);
    }

    @NonNull
    @Override
    public String toString() {
        return "PlaceDetailViewState{" +
                "user=" + user +
                ", place=" + place +
                ", workmates=" + workmates +
                '}';
    }
}
