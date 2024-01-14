package com.example.go4lunch.data.models.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlacesDetailsResponse {

    @NonNull
    @SerializedName("results")
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
}
