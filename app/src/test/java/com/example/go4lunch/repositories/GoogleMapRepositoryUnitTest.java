package com.example.go4lunch.repositories;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.data.api.GoogleMapApi;
import com.example.go4lunch.data.models.map.MapPlace;
import com.example.go4lunch.data.models.map.NearbySearchResponse;
import com.example.go4lunch.data.models.map.PlaceAutocompletePrediction;
import com.example.go4lunch.data.models.map.PlaceAutocompleteResponse;
import com.example.go4lunch.data.models.map.PlacesDetailsResponse;
import com.example.go4lunch.data.services.GoogleMapRepository;
import com.example.go4lunch.utils.FakeDataUtil;
import com.example.go4lunch.utils.LiveDataTestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RunWith(JUnit4.class)
public class GoogleMapRepositoryUnitTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private final GoogleMapApi googleMapApi = mock();

    private GoogleMapRepository googleMapRepository;

    private final int MAP_API_RADIUS = 1500;
    private final String MAP_API_ESTABLISHMENT_TYPE = "restaurant";
    private final String apiKey = BuildConfig.MAPS_API_KEY;

    @Before
    public void setup() {
        googleMapRepository = new GoogleMapRepository(googleMapApi);
    }

    @Test
    public void getNearbyPlaceLiveData_success() throws InterruptedException {

        double latitude = 1.0;
        double longitude = 2.0;
        String location = latitude + "," + longitude;
        List<MapPlace> expectedNearbyPlaces = FakeDataUtil.getNearbyPlaces();

        Call<NearbySearchResponse> nearbySearchResponseCallMock = mock();
        Response<NearbySearchResponse> responseMock = mock();
        NearbySearchResponse nearbySearchResponseMock = new NearbySearchResponse(expectedNearbyPlaces, null, null);

        when(googleMapApi.getNearbyPlace(location, MAP_API_RADIUS, MAP_API_ESTABLISHMENT_TYPE, apiKey)).thenReturn(nearbySearchResponseCallMock);
        when(responseMock.body()).thenReturn(nearbySearchResponseMock);
        when(responseMock.isSuccessful()).thenReturn(true);

        LiveData<List<MapPlace>> mapPlaceLiveData = googleMapRepository.getNearbyPlaceLiveData(latitude, longitude);

        ArgumentCaptor<Callback<NearbySearchResponse>> argumentCaptor = ArgumentCaptor.captor();
        verify(nearbySearchResponseCallMock).enqueue(argumentCaptor.capture());
        Callback<NearbySearchResponse> nearbySearchResponseCallback = argumentCaptor.getValue();

        nearbySearchResponseCallback.onResponse(nearbySearchResponseCallMock, responseMock);

        List<MapPlace> nearbyPlaces = LiveDataTestUtil.getOrAwaitValue(mapPlaceLiveData);

        verifyNoMoreInteractions(nearbySearchResponseCallMock);
        assertEquals(expectedNearbyPlaces, nearbyPlaces);
    }

    @Test
    public void getPlaceAutocomplete_success() throws InterruptedException {

        String searchInput = "Ma recherche";
        double latitude = 1.0;
        double longitude = 2.0;
        String location = latitude + "," + longitude;
        List<PlaceAutocompletePrediction> expectedPredictions = FakeDataUtil.getPlacePredictions();

        Call<PlaceAutocompleteResponse> placeAutocompleteResponseCallMock = mock();
        Response<PlaceAutocompleteResponse> responseMock = mock();
        PlaceAutocompleteResponse placeAutocompleteResponseMock = new PlaceAutocompleteResponse(expectedPredictions, "ok", null, null);

        when(googleMapApi.getPlaceAutocomplete(searchInput, location, MAP_API_RADIUS, MAP_API_ESTABLISHMENT_TYPE, null, apiKey)).thenReturn(placeAutocompleteResponseCallMock);
        when(responseMock.body()).thenReturn(placeAutocompleteResponseMock);
        when(responseMock.isSuccessful()).thenReturn(true);

        LiveData<List<PlaceAutocompletePrediction>> predictionsLiveData = googleMapRepository.getPlaceAutocomplete(searchInput, latitude, longitude, null);

        ArgumentCaptor<Callback<PlaceAutocompleteResponse>> argumentCaptor = ArgumentCaptor.captor();
        verify(placeAutocompleteResponseCallMock).enqueue(argumentCaptor.capture());
        Callback<PlaceAutocompleteResponse> placeAutocompleteResponseCallback = argumentCaptor.getValue();

        placeAutocompleteResponseCallback.onResponse(placeAutocompleteResponseCallMock, responseMock);

        List<PlaceAutocompletePrediction> predictions = LiveDataTestUtil.getOrAwaitValue(predictionsLiveData);

        verifyNoMoreInteractions(placeAutocompleteResponseCallMock);
        assertEquals(expectedPredictions, predictions);

    }

    @Test
    public void getPlaceDetailsLiveData_success() throws InterruptedException {
        String placeId = "place1";
        MapPlace expectedPlace = FakeDataUtil.getMapPlace();

        Call<PlacesDetailsResponse> placesDetailsResponseCallMock = mock();
        Response<PlacesDetailsResponse> responseMock = mock();
        PlacesDetailsResponse placesDetailsResponseMock = new PlacesDetailsResponse(expectedPlace, "ok", null);

        when(googleMapApi.getPlaceDetails(placeId, null, apiKey)).thenReturn(placesDetailsResponseCallMock);
        when(responseMock.body()).thenReturn(placesDetailsResponseMock);
        when(responseMock.isSuccessful()).thenReturn(true);

        LiveData<MapPlace> placeDetailsLiveData = googleMapRepository.getPlaceDetailsLiveData(placeId, null);

        ArgumentCaptor<Callback<PlacesDetailsResponse>> argumentCaptor = ArgumentCaptor.captor();
        verify(placesDetailsResponseCallMock).enqueue(argumentCaptor.capture());
        Callback<PlacesDetailsResponse> placesDetailsResponseCallbackMock = argumentCaptor.getValue();

        placesDetailsResponseCallbackMock.onResponse(placesDetailsResponseCallMock, responseMock);

        MapPlace placeDetails = LiveDataTestUtil.getOrAwaitValue(placeDetailsLiveData);

        verifyNoMoreInteractions(placesDetailsResponseCallMock);
        assertEquals(expectedPlace, placeDetails);
    }

}
