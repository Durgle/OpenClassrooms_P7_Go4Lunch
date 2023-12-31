package com.example.go4lunch.data.models.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NearbySearchResponse {

    @NonNull
    @SerializedName("results")
    @Expose
    private final List<Place> places;

    @SerializedName("error_message")
    @Expose
    private final String errorMessage;

    @SerializedName("info_messages")
    @Expose
    private final List<String> infoMessages;

    public NearbySearchResponse(@NonNull List<Place> places, @Nullable String errorMessage, @Nullable List<String> infoMessages) {
        this.places = places;
        this.errorMessage = errorMessage;
        this.infoMessages = infoMessages;
    }

    @NonNull
    public List<Place> getPlaces() {
        return places;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<String> getInfoMessages() {
        return infoMessages;
    }
}
