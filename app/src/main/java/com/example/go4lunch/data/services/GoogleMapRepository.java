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
import com.example.go4lunch.data.models.map.PlaceAutocompletePrediction;
import com.example.go4lunch.data.models.map.PlaceAutocompleteResponse;
import com.example.go4lunch.data.models.map.PlacesDetailsResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Manage call to google map api
 */
public class GoogleMapRepository {

    final int RADIUS = 1500;
    @NonNull
    final String ESTABLISHMENT_TYPE = "restaurant";

    private final GoogleMapApi mGoogleMapApi;
    private final Map<String, NearbySearchResponse> nearbyPlaceFetchedResponses = new HashMap<>();
    private final Map<String, PlacesDetailsResponse> placeDetailFetchedResponses = new HashMap<>();
    private final Map<String, PlaceAutocompleteResponse> placeAutocompleteFetchedResponses = new HashMap<>();
    private final String apiKey;

    public GoogleMapRepository(GoogleMapApi googleMapApi) {

        mGoogleMapApi = googleMapApi;
        apiKey = BuildConfig.MAPS_API_KEY;
    }

    /**
     * Get all nearby place
     *
     * @param latitude Latitude
     * @param longitude Longitude
     * @return List of place live data
     */
    public LiveData<List<Place>> getNearbyPlaceLiveData(double latitude, double longitude) {

        MutableLiveData<List<Place>> placesLiveData = new MutableLiveData<>();
        String location = latitude + "," + longitude;
        NearbySearchResponse response = nearbyPlaceFetchedResponses.get(location);

        if (response != null) {
            placesLiveData.setValue(response.getPlaces());
        } else {

            mGoogleMapApi.getNearbyPlace(location, RADIUS, ESTABLISHMENT_TYPE, apiKey).enqueue(new Callback<NearbySearchResponse>() {
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

    /**
     * Get place autocomplete
     *
     * @return List of place autocomplete prediction live data
     */
    public LiveData<List<PlaceAutocompletePrediction>> getPlaceAutocomplete(String input, double latitude, double longitude, @Nullable String language) {

        MutableLiveData<List<PlaceAutocompletePrediction>> autocompleteLiveData = new MutableLiveData<>();
        String location = latitude + "," + longitude;
        PlaceAutocompleteResponse response = placeAutocompleteFetchedResponses.get(input+"|"+location);

        if (response != null) {
            autocompleteLiveData.setValue(response.getPredictions());
        } else {

            mGoogleMapApi.getPlaceAutocomplete(input,location,RADIUS, ESTABLISHMENT_TYPE,language,apiKey).enqueue(new Callback<PlaceAutocompleteResponse>() {
                @Override
                public void onResponse(@NonNull Call<PlaceAutocompleteResponse> call, @NonNull Response<PlaceAutocompleteResponse> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        placeAutocompleteFetchedResponses.put(input+"|"+location, response.body());
                        autocompleteLiveData.setValue(response.body().getPredictions());
                    } else {
                        Log.e("GoogleMapRepository", response.message() + " responseCode:" + response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PlaceAutocompleteResponse> call, @NonNull Throwable t) {
                    Log.e("GoogleMapRepository", t.toString());
                    autocompleteLiveData.setValue(null);
                }
            });
        }

        return autocompleteLiveData;
    }

    /**
     * Get all information for the given place
     *
     * @param placeId Place id
     * @param language Language
     * @return Place Detail live data
     */
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
