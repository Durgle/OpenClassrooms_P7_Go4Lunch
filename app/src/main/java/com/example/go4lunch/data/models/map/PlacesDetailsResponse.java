package com.example.go4lunch.data.models.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class PlacesDetailsResponse {

    @NonNull
    @SerializedName("result")
    @Expose
    private final Place place;

    @SerializedName("status")
    @Expose
    private final String status;

    @SerializedName("info_messages")
    @Expose
    private final List<String> infoMessages;

    public PlacesDetailsResponse(@NonNull Place place, @NonNull String status, @Nullable List<String> infoMessages) {
        this.place = place;
        this.status = status;
        this.infoMessages = infoMessages;
    }

    @NonNull
    public Place getPlace() {
        return place;
    }

    @NonNull
    public String getStatus() {
        return status;
    }

    public List<String> getInfoMessages() {
        return infoMessages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlacesDetailsResponse that = (PlacesDetailsResponse) o;
        return place.equals(that.place) && Objects.equals(status, that.status) && Objects.equals(infoMessages, that.infoMessages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(place, status, infoMessages);
    }

    @NonNull
    @Override
    public String toString() {
        return "PlacesDetailsResponse{" +
                "place=" + place +
                ", status='" + status + '\'' +
                ", infoMessages=" + infoMessages +
                '}';
    }
}
