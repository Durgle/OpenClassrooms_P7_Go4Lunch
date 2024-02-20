package com.example.go4lunch.ui.placeDetail.viewState;

import androidx.annotation.NonNull;

import java.util.Objects;

import javax.annotation.Nullable;

public class WorkmateState {

    @NonNull
    private final String id;
    @Nullable
    private final String picture;
    @NonNull
    private final String workmateName;

    public WorkmateState(@NonNull String id, @NonNull String workmateName, @Nullable String picture) {
        this.id = id;
        this.picture = picture;
        this.workmateName = workmateName;
    }

    @Nullable
    public String getPicture() {
        return picture;
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
        WorkmateState that = (WorkmateState) o;
        return Objects.equals(id, that.id) && Objects.equals(picture, that.picture) && Objects.equals(workmateName, that.workmateName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, picture, workmateName);
    }

    @NonNull
    @Override
    public String toString() {
        return "WorkmateViewState{" +
                "id='" + id + '\'' +
                ", picture='" + picture + '\'' +
                ", workmateName='" + workmateName + '\'' +
                '}';
    }
}
