package com.example.go4lunch.ui.workmate;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.R;
import com.example.go4lunch.data.models.firestore.Place;
import com.example.go4lunch.data.models.firestore.User;
import com.example.go4lunch.data.services.UserRepository;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

public class WorkmateViewState {

    @NonNull
    private final String id;
    @Nullable
    private final String picture;
    @Nullable
    private final Place place;
    @NonNull
    private final String workmateName;

    public WorkmateViewState(@NonNull String id,@NonNull String workmateName, @Nullable Place place, @Nullable String picture) {
        this.id = id;
        this.picture = picture;
        this.place = place;
        this.workmateName = workmateName;
    }

    @Nullable
    public String getPicture() {
        return picture;
    }

    @Nullable
    public Place getPlace() {
        return place;
    }

    @NonNull
    public String getWorkmateName() {
        return workmateName;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkmateViewState that = (WorkmateViewState) o;
        return Objects.equals(id, that.id) && Objects.equals(picture, that.picture) && Objects.equals(place, that.place) && Objects.equals(workmateName, that.workmateName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, picture, place, workmateName);
    }

    @NonNull
    @Override
    public String toString() {
        return "WorkmateViewState{" +
                "id='" + id + '\'' +
                ", picture='" + picture + '\'' +
                ", place='" + place + '\'' +
                ", workmateName='" + workmateName + '\'' +
                '}';
    }
}
