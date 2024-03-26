package com.example.go4lunch.data.models.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class NearbySearchResponse {

    @NonNull
    @SerializedName("results")
    @Expose
    private final List<MapPlace> places;

    @SerializedName("error_message")
    @Expose
    private final String errorMessage;

    @SerializedName("info_messages")
    @Expose
    private final List<String> infoMessages;

    public NearbySearchResponse(@NonNull List<MapPlace> places, @Nullable String errorMessage, @Nullable List<String> infoMessages) {
        this.places = places;
        this.errorMessage = errorMessage;
        this.infoMessages = infoMessages;
    }

    @NonNull
    public List<MapPlace> getPlaces() {
        return places;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<String> getInfoMessages() {
        return infoMessages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NearbySearchResponse that = (NearbySearchResponse) o;
        return places.equals(that.places) && Objects.equals(errorMessage, that.errorMessage) && Objects.equals(infoMessages, that.infoMessages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(places, errorMessage, infoMessages);
    }

    @NonNull
    @Override
    public String toString() {
        return "NearbySearchResponse{" +
                "places=" + places +
                ", errorMessage='" + errorMessage + '\'' +
                ", infoMessages=" + infoMessages +
                '}';
    }
}
