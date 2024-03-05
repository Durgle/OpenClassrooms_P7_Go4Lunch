package com.example.go4lunch.utils;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.R;
import com.example.go4lunch.data.models.map.PlaceOpeningHours;

/**
 * Class with some useful function for google map api
 */
public class GoogleMapApiUtils {

    /**
     * Create google map photo url for the given photo
     *
     * @param photoReference Photo reference
     * @param maxWidth Max width
     * @param maxHeight Maw height
     * @return url from google map api for the photo
     */
    @SuppressWarnings("SpellCheckingInspection")
    @NonNull
    public static String createPhotoUrl(@NonNull String photoReference, int maxWidth, int maxHeight) {
        return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=" + maxWidth + "&maxheight=" + maxHeight + "&photo_reference=" + photoReference + "&key=" + BuildConfig.MAPS_API_KEY;
    }

    /**
     * Create a business status with place opening hours
     *
     * @param placeOpeningHours Place opening hours
     * @return Return Open, close or null if Place opening hours is null
     */
    @StringRes
    public static Integer createStatus(PlaceOpeningHours placeOpeningHours) {
        if (placeOpeningHours != null) {
            if (placeOpeningHours.isOpenNow() == Boolean.TRUE) {
                return R.string.place_status_open;
            } else {
                return R.string.place_status_close;
            }
        }
        return null;
    }

    /**
     * Format rating with max 3 stars
     *
     * @param rating rating
     * @return Formatted rating between 1 star and 3 stars
     */
    @NonNull
    public static String formatStarRating(@Nullable Float rating) {

        StringBuilder stars = new StringBuilder();
        if (rating != null) {
            int numberOfStars = (int) ((rating * 3) / 5);

            for (int i = 0; i < numberOfStars; i++) {
                stars.append("★");
            }
        }

        return stars.toString();
    }

    /**
     * Format distance between two location
     *
     * @param currentLocation Current location
     * @param placeLocation Pace location
     * @return Distance in meter
     */
    @NonNull
    public static String formatDistance(@Nullable Location currentLocation, @Nullable Location placeLocation) {

        if (placeLocation != null && currentLocation != null) {
            return ((int) currentLocation.distanceTo(placeLocation)) + "m";
        }
        return "";
    }

}
