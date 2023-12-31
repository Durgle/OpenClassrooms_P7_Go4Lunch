package com.example.go4lunch.data.services;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.data.api.GoogleMapApi;
import com.example.go4lunch.data.models.map.NearbySearchResponse;
import com.example.go4lunch.data.models.map.Place;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleMapRepository {

    private final GoogleMapApi mGoogleMapApi;

    private NearbySearchResponse alreadyFetchedResponses = null;

    private String mApiKey;

    public GoogleMapRepository(GoogleMapApi googleMapApi) {

        mGoogleMapApi = googleMapApi;
        mApiKey = BuildConfig.MAPS_API_KEY;
    }

    public LiveData<List<Place>> getNearbyPlaceLiveData(double latitude,double longitude) {

        MutableLiveData<List<Place>> placesLiveData = new MutableLiveData<>();
        NearbySearchResponse response = alreadyFetchedResponses;

        if (response != null) {
            placesLiveData.setValue(response.getPlaces());
        } else {

            mGoogleMapApi.getNearbyPlace(latitude + "," + longitude,1500,"restaurant",mApiKey).enqueue(new Callback<NearbySearchResponse>() {
                @Override
                public void onResponse(@NonNull Call<NearbySearchResponse> call, @NonNull Response<NearbySearchResponse> response) {
                    if (response.body() != null && response.isSuccessful()) {

                        alreadyFetchedResponses = response.body();
                        placesLiveData.setValue(alreadyFetchedResponses.getPlaces());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<NearbySearchResponse> call, @NonNull Throwable t) {
                    placesLiveData.setValue(null);
                }
            });
        }

        return placesLiveData;
    }

}
