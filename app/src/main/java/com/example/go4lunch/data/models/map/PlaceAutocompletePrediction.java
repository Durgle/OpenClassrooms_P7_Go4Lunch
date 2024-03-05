package com.example.go4lunch.data.models.map;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class PlaceAutocompletePrediction {

    @NonNull
    @SerializedName("description")
    @Expose
    private final String description;

    @NonNull
    @SerializedName("place_id")
    @Expose
    private final String placeId;

    public PlaceAutocompletePrediction(@NonNull String description, @NonNull String placeId) {
        this.description = description;
        this.placeId = placeId;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    @NonNull
    public String getPlaceId() {
        return placeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceAutocompletePrediction that = (PlaceAutocompletePrediction) o;
        return Objects.equals(description, that.description) && Objects.equals(placeId, that.placeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, placeId);
    }

    @NonNull
    @Override
    public String toString() {
        return "PlaceAutocompletePrediction{" +
                "description='" + description + '\'' +
                ", placeId='" + placeId + '\'' +
                '}';
    }
}
