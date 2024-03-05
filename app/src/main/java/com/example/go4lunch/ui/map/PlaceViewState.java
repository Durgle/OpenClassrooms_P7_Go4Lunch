package com.example.go4lunch.ui.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

public class PlaceViewState {

    @NonNull
    private final String placeId;
    @NonNull
    private final String placeName;
    private final long workmateCount;
    @Nullable
    private final LatLng location;

    public PlaceViewState(@NonNull String placeId, @NonNull String placeName, long workmateCount, @Nullable LatLng location) {
        this.placeId = placeId;
        this.placeName = placeName;
        this.workmateCount = workmateCount;
        this.location = location;
    }

    @NonNull
    public String getPlaceId() {
        return placeId;
    }

    @NonNull
    public String getPlaceName() {
        return placeName;
    }

    public long getWorkmateCount() {
        return workmateCount;
    }

    @Nullable
    public LatLng getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceViewState that = (PlaceViewState) o;
        return workmateCount == that.workmateCount && Objects.equals(placeId, that.placeId) && Objects.equals(placeName, that.placeName) && Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeId, placeName, workmateCount, location);
    }

    @NonNull
    @Override
    public String toString() {
        return "PlaceViewState{" +
                "placeId='" + placeId + '\'' +
                ", placeName='" + placeName + '\'' +
                ", workmateCount=" + workmateCount +
                ", location=" + location +
                '}';
    }
}
