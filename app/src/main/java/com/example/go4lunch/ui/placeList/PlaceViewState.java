package com.example.go4lunch.ui.placeList;

import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.go4lunch.data.models.map.PlaceOpeningHours;
import com.example.go4lunch.utils.GoogleMapApiUtils;

import java.util.Objects;

public class PlaceViewState {

    private final String id;

    private final String name;

    private final String address;

    private final Integer status;

    private final String distance;

    private final String rating;

    private final String photo;

    public PlaceViewState(
            @NonNull String id,
            @NonNull String name,
            @Nullable String address,
            @Nullable PlaceOpeningHours placeOpeningHours,
            Location currentLocation,
            Location placeLocation,
            @Nullable Float rating,
            @Nullable String photoReference
    ) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.status = GoogleMapApiUtils.createStatus(placeOpeningHours);
        this.distance = GoogleMapApiUtils.formatDistance(currentLocation,placeLocation);
        this.rating = GoogleMapApiUtils.formatStarRating(rating);
        this.photo = photoReference != null ? GoogleMapApiUtils.createPhotoUrl(photoReference,100,100) : null;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Nullable
    public String getAddress() {
        return address;
    }

    @Nullable
    public String getStatus(@NonNull Context context) {
        if(status != null){
            return context.getString(status);
        } else {
            return "";
        }
    }

    @Nullable
    public String getDistance() {
        return distance;
    }

    @Nullable
    public String getRating() {
        return rating;
    }

    @Nullable
    public String getPhoto() {
        return photo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceViewState that = (PlaceViewState) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(address, that.address) && Objects.equals(status, that.status) && Objects.equals(distance, that.distance) && Objects.equals(rating, that.rating) && Objects.equals(photo, that.photo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, status, distance, rating, photo);
    }

    @NonNull
    @Override
    public String toString() {
        return "PlaceListViewState{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", status='" + status + '\'' +
                ", distance='" + distance + '\'' +
                ", rating='" + rating + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
