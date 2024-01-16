package com.example.go4lunch.ui.placeList;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.data.models.map.PlaceOpeningHours;

import java.util.Objects;

public class PlaceViewState {

    private final String id;

    private final String name;

    private final String address;

    private final String status;

    private final String distance;

    private final String rating;

    private final String photo;

    public PlaceViewState(
            @NonNull String id,
            @NonNull String name,
            @Nullable String address,
            @Nullable PlaceOpeningHours placeOpeningHours,
            Double currentLatitude,
            Double currentLongitude,
            Double placeLatitude,
            Double placeLongitude,
            @Nullable Float rating,
            @Nullable String photoReference
    ) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.status = createStatus(placeOpeningHours);
        this.distance = formatDistance(currentLatitude,currentLongitude,placeLatitude,placeLongitude);
        this.rating = formatStarRating(rating);
        this.photo = createPhotoUrl(photoReference);
    }

    public @NonNull String createStatus(PlaceOpeningHours placeOpeningHours) {
        if(placeOpeningHours != null){
            if( placeOpeningHours.isOpenNow() == Boolean.TRUE){
                return "Open";
            } else {
                return "Close";
            }
        }
        return "";
    }

    @NonNull
    public String formatDistance(
        Double currentLatitude,
        Double currentLongitude,
        Double placeLatitude,
        Double placeLongitude
    ) {

        if(currentLatitude != null && currentLongitude != null && placeLatitude != null && placeLongitude != null){
            float[] result = new float[1];
            Location.distanceBetween(currentLatitude,currentLongitude,placeLatitude,placeLongitude,result);
            return ((int)result[0])+"m";
        }
        return "";
    }

    @Nullable
    public String createPhotoUrl(String photoReference) {
        String url;
        if(photoReference != null){
            url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=100&maxheight=100&photo_reference="+ photoReference +"&key="+ BuildConfig.MAPS_API_KEY;
        } else {
            url = null;
        }
        return url;
    }

    @NonNull
    public String formatStarRating(Float rating) {

        StringBuilder stars = new StringBuilder();
        if(rating != null){
            int numberOfStars = (int)((rating * 3) / 5);

            for (int i = 0; i < numberOfStars; i++) {
                stars.append("\u2605");
            }
        }

        return stars.toString();
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
    public String getStatus() {
        return status;
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
