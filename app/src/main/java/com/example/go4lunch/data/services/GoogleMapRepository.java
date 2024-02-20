package com.example.go4lunch.data.services;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.data.api.GoogleMapApi;
import com.example.go4lunch.data.models.map.NearbySearchResponse;
import com.example.go4lunch.data.models.map.Place;
import com.example.go4lunch.data.models.map.PlacesDetailsResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleMapRepository {

    private final GoogleMapApi mGoogleMapApi;

    private final Map<String, NearbySearchResponse> nearbyPlaceFetchedResponses = new HashMap<>();
    private final Map<String, PlacesDetailsResponse> placeDetailFetchedResponses = new HashMap<>();
    private final String apiKey;

    public GoogleMapRepository(GoogleMapApi googleMapApi) {

        mGoogleMapApi = googleMapApi;
        apiKey = BuildConfig.MAPS_API_KEY;
    }

    public LiveData<List<Place>> getNearbyPlaceLiveData(double latitude, double longitude) {

        MutableLiveData<List<Place>> placesLiveData = new MutableLiveData<>();
        String location = latitude + "," + longitude;
        NearbySearchResponse response = nearbyPlaceFetchedResponses.get(location);

        if (response != null) {
            placesLiveData.setValue(response.getPlaces());
        } else {

            mGoogleMapApi.getNearbyPlace(location, 1500, "restaurant", apiKey).enqueue(new Callback<NearbySearchResponse>() {
                @Override
                public void onResponse(@NonNull Call<NearbySearchResponse> call, @NonNull Response<NearbySearchResponse> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        nearbyPlaceFetchedResponses.put(location, response.body());
                        placesLiveData.setValue(response.body().getPlaces());
                    } else {
                        Log.e("GoogleMapRepository", response.message() + " responseCode:" + response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<NearbySearchResponse> call, @NonNull Throwable t) {
                    Log.e("GoogleMapRepository", t.toString());
                    placesLiveData.setValue(null);
                }
            });
        }

        return placesLiveData;
    }

    public LiveData<Place> getPlaceDetailsLiveData(@NonNull String placeId, @Nullable String language) {

        MutableLiveData<Place> placesLiveData = new MutableLiveData<>();
        PlacesDetailsResponse response = placeDetailFetchedResponses.get(placeId);

        if (response != null) {
            placesLiveData.setValue(response.getPlace());
        } else {

            mGoogleMapApi.getPlaceDetails(placeId, language, apiKey).enqueue(new Callback<PlacesDetailsResponse>() {
                @Override
                public void onResponse(@NonNull Call<PlacesDetailsResponse> call, @NonNull Response<PlacesDetailsResponse> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        placeDetailFetchedResponses.put(placeId, response.body());
                        placesLiveData.setValue(response.body().getPlace());
                    } else {
                        Log.e("GoogleMapRepository", response.message() + " responseCode:" + response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PlacesDetailsResponse> call, @NonNull Throwable t) {
                    Log.e("GoogleMapRepository", t.toString());
                    placesLiveData.setValue(null);
                }
            });
        }

        return placesLiveData;

    }

}
