package com.example.go4lunch.utils;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.data.models.map.PlaceOpeningHours;

public class GoogleMapApiUtils {

    @SuppressWarnings("SpellCheckingInspection")
    @NonNull
    public static String createPhotoUrl(@NonNull String photoReference, int maxWidth, int maxHeight) {
        return "https://maps.googleapis.com/maps/api/place/photo?maxwidth="+maxWidth+"&maxheight="+maxHeight+"&photo_reference="+ photoReference +"&key="+ BuildConfig.MAPS_API_KEY;
    }

    public static @NonNull String createStatus(PlaceOpeningHours placeOpeningHours) {
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
    public static String formatStarRating(@Nullable Float rating) {

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
    public static String formatDistance(@Nullable Location currentLocation, @Nullable Location placeLocation) {

        if(placeLocation != null && currentLocation != null){
            return ((int)currentLocation.distanceTo(placeLocation))+"m";
        }
        return "";
    }

}
