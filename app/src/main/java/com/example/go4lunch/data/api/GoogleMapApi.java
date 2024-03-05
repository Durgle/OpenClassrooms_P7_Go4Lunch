package com.example.go4lunch.data.api;

import androidx.annotation.NonNull;

import com.example.go4lunch.data.models.map.NearbySearchResponse;
import com.example.go4lunch.data.models.map.PlaceAutocompleteResponse;
import com.example.go4lunch.data.models.map.PlacesDetailsResponse;

import retrofit2.Call;

import retrofit2.http.GET;
import retrofit2.http.Query;

@SuppressWarnings("SpellCheckingInspection")
public interface GoogleMapApi {

    @GET("place/nearbysearch/json")
    Call<NearbySearchResponse> getNearbyPlace(
            @Query("location") @NonNull String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("key") String key
    );

    @GET("place/details/json")
    Call<PlacesDetailsResponse> getPlaceDetails(
            @Query("place_id") @NonNull String placeId,
            @Query("language") String language,
            @Query("key") @NonNull String key
    );

    @GET("place/autocomplete/json")
    Call<PlaceAutocompleteResponse> getPlaceAutocomplete(
            @Query("input") @NonNull String input,
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("language") String language,
            @Query("key") @NonNull String key
    );
}
