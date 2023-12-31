package com.example.go4lunch.data.api;

import com.example.go4lunch.data.models.map.NearbySearchResponse;

import retrofit2.Call;

import retrofit2.http.GET;
import retrofit2.http.Query;

@SuppressWarnings("SpellCheckingInspection")
public interface GoogleMapApi {

    @GET("place/nearbysearch/json")
    Call<NearbySearchResponse> getNearbyPlace(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("key") String key
    );
}
