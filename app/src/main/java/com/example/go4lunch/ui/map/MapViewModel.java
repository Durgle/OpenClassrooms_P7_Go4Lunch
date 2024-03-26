package com.example.go4lunch.ui.map;

import android.annotation.SuppressLint;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.PermissionChecker;
import com.example.go4lunch.data.models.firestore.User;
import com.example.go4lunch.data.models.map.MapPlace;
import com.example.go4lunch.data.models.map.PlaceAutocompletePrediction;
import com.example.go4lunch.data.services.GoogleMapRepository;
import com.example.go4lunch.data.services.LocationRepository;
import com.example.go4lunch.data.services.SearchRepository;
import com.example.go4lunch.data.services.UserRepository;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

public class MapViewModel extends ViewModel {

    private final GoogleMapRepository googleMapRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final PermissionChecker permissionChecker;
    private final SearchRepository searchRepository;
    private final MediatorLiveData<List<PlaceViewState>> nearbyPlaces = new MediatorLiveData<>();

    @NonNull
    public static PlaceViewState mapPlace(@NonNull MapPlace place, @NonNull Map<String, Long> workmateByPlace) {
        String placeId = place.getId();
        Long workmateCount = workmateByPlace.getOrDefault(placeId, 0L);
        LatLng location;
        if (place.getGeometry() != null) {
            location = new LatLng(
                    place.getGeometry().getLocation().getLat(),
                    place.getGeometry().getLocation().getLng()
            );
        } else {
            location = null;
        }
        return new PlaceViewState(placeId, place.getName(), (workmateCount != null) ? workmateCount : 0L, location);
    }

    public MapViewModel(
            GoogleMapRepository googleMapRepository,
            LocationRepository locationRepository,
            UserRepository userRepository,
            PermissionChecker permissionChecker,
            SearchRepository searchRepository
    ) {
        this.googleMapRepository = googleMapRepository;
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
        this.permissionChecker = permissionChecker;
        this.searchRepository = searchRepository;

        LiveData<Location> locationLiveData = locationRepository.getLocationLiveData();
        LiveData<List<User>> workmatesLiveData = this.userRepository.getAllWorkmates();
        LiveData<String> searchLiveData = this.searchRepository.getSearch();

        LiveData<List<PlaceAutocompletePrediction>> autocompleteLiveData = Transformations.switchMap(searchLiveData, search -> {
            Location location = locationLiveData.getValue();
            if (search != null && location != null) {
                return this.googleMapRepository.getPlaceAutocomplete(search, location.getLatitude(), location.getLongitude(), null);
            }
            return new MutableLiveData<>(null);
        });

        LiveData<List<MapPlace>> nearbyPlacesLiveData = Transformations.switchMap(locationLiveData, location -> {
            if (location != null) {
                return this.googleMapRepository.getNearbyPlaceLiveData(location.getLatitude(), location.getLongitude());
            }
            return new MutableLiveData<>(new ArrayList<>());
        });

        this.nearbyPlaces.addSource(nearbyPlacesLiveData, places -> combine(places, workmatesLiveData.getValue(), autocompleteLiveData.getValue()));
        this.nearbyPlaces.addSource(workmatesLiveData, workmateList -> combine(nearbyPlacesLiveData.getValue(), workmateList, autocompleteLiveData.getValue()));
        this.nearbyPlaces.addSource(autocompleteLiveData, autocompleteResult -> combine(nearbyPlacesLiveData.getValue(), workmatesLiveData.getValue(), autocompleteResult));
    }

    private void combine(
            @Nullable List<MapPlace> places,
            @Nullable List<User> workmates,
            @Nullable List<PlaceAutocompletePrediction> predictions
    ) {

        if (places != null && workmates != null) {
            Map<String, Long> workmateByPlace = workmates.stream()
                    .filter(workmate -> workmate.getPlace() != null)
                    .collect(Collectors.groupingBy(workmate -> workmate.getPlace().getUid(), Collectors.counting()));

            List<PlaceViewState> placeViewStates;
            if (predictions != null) {
                List<String> predictedIds = predictions.stream()
                        .map(PlaceAutocompletePrediction::getPlaceId)
                        .collect(Collectors.toList());

                placeViewStates = places.stream()
                        .filter(place -> predictedIds.contains(place.getId()))
                        .map(place -> mapPlace(place, workmateByPlace)).collect(Collectors.toList());
            } else {
                placeViewStates = places.stream()
                        .map(place -> mapPlace(place, workmateByPlace))
                        .collect(Collectors.toList());
            }

            this.nearbyPlaces.setValue(placeViewStates);
        } else {
            this.nearbyPlaces.setValue(new ArrayList<>());
        }

    }

    @SuppressLint("MissingPermission")
    public void refresh() {
        boolean hasGpsPermission = permissionChecker.hasLocationPermission();

        if (hasGpsPermission) {
            locationRepository.startLocationRequest();
        } else {
            locationRepository.stopLocationRequest();
        }
    }

    @NonNull
    public LiveData<List<PlaceViewState>> getNearbyPlaces() {
        return this.nearbyPlaces;
    }

    public LiveData<Location> getCurrentLocation() {
        return locationRepository.getLocationLiveData();
    }
}
