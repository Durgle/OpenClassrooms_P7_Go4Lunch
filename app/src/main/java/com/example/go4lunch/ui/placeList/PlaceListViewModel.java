package com.example.go4lunch.ui.placeList;

import android.annotation.SuppressLint;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.PermissionChecker;
import com.example.go4lunch.data.models.firestore.User;
import com.example.go4lunch.data.models.map.Place;
import com.example.go4lunch.data.models.map.PlaceAutocompletePrediction;
import com.example.go4lunch.data.services.GoogleMapRepository;
import com.example.go4lunch.data.services.LocationRepository;
import com.example.go4lunch.data.services.SearchRepository;
import com.example.go4lunch.data.services.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class PlaceListViewModel extends ViewModel {

    private final GoogleMapRepository googleMapRepository;
    private final LocationRepository locationRepository;
    private final SearchRepository searchRepository;
    private final PermissionChecker permissionChecker;
    private final UserRepository userRepository;
    private final LiveData<List<Place>> nearbyPlace;
    private final MediatorLiveData<List<PlaceViewState>> formattedNearbyPlace = new MediatorLiveData<>();

    public PlaceListViewModel(
            GoogleMapRepository googleMapRepository,
            LocationRepository locationRepository,
            PermissionChecker permissionChecker,
            UserRepository userRepository,
            SearchRepository searchRepository
    ) {
        this.googleMapRepository = googleMapRepository;
        this.locationRepository = locationRepository;
        this.permissionChecker = permissionChecker;
        this.userRepository = userRepository;
        this.searchRepository = searchRepository;

        LiveData<Location> locationLiveData = locationRepository.getLocationLiveData();
        LiveData<List<User>> workmates = userRepository.getAllWorkmates();
        LiveData<String> searchLiveData = searchRepository.getSearch();

        LiveData<List<PlaceAutocompletePrediction>> autocompleteLiveData = Transformations.switchMap(searchLiveData, search -> {
            Location location = locationLiveData.getValue();
            if (search != null && location != null) {
                return this.googleMapRepository.getPlaceAutocomplete(search, location.getLatitude(), location.getLongitude(), null);
            }
            return new MutableLiveData<>(null);
        });

        nearbyPlace = Transformations.switchMap(locationLiveData, location -> {
            if (location != null) {
                return this.googleMapRepository.getNearbyPlaceLiveData(location.getLatitude(), location.getLongitude());
            }
            return new MutableLiveData<>(new ArrayList<>());
        });

        formattedNearbyPlace.addSource(nearbyPlace, placeList -> combine(placeList, locationLiveData.getValue(), workmates.getValue(), autocompleteLiveData.getValue()));
        formattedNearbyPlace.addSource(locationLiveData, location -> combine(nearbyPlace.getValue(), location, workmates.getValue(), autocompleteLiveData.getValue()));
        formattedNearbyPlace.addSource(workmates, workmateList -> combine(nearbyPlace.getValue(), locationLiveData.getValue(), workmateList, autocompleteLiveData.getValue()));
        formattedNearbyPlace.addSource(autocompleteLiveData, autocompletePredictions -> combine(nearbyPlace.getValue(), locationLiveData.getValue(), workmates.getValue(), autocompletePredictions));

    }

    private void combine(
            @Nullable List<Place> placeList,
            @Nullable Location currentLocation,
            @Nullable List<User> workmateList,
            @Nullable List<PlaceAutocompletePrediction> predictions
    ) {

        if (placeList != null && currentLocation != null && workmateList != null) {
            List<PlaceViewState> formattedList = new ArrayList<>();
            List<String> predictedIds;
            if(predictions != null) {
                predictedIds = predictions.stream()
                        .map(PlaceAutocompletePrediction::getPlaceId)
                        .collect(Collectors.toList());
            } else {
                predictedIds = null;
            }
            for (Place place : placeList) {
                if(predictedIds == null || predictedIds.contains(place.getId())) {
                    formattedList.add(mapPlaceData(place, currentLocation, workmateList));
                }
            }
            formattedNearbyPlace.setValue(formattedList);
        } else {
            formattedNearbyPlace.setValue(new ArrayList<>());
        }
    }

    private PlaceViewState mapPlaceData(@NonNull Place place, @NonNull Location currentLocation, @NonNull List<User> workmatesList) {

        String photoReference = (place.getPhotos() != null) ? place.getPhotos().get(0).getPhotoReference() : null;
        Location placeLocation;

        long workmateCount = workmatesList.stream()
                .filter(workmate -> workmate.getPlace() != null && Objects.equals(workmate.getPlace().getUid(), place.getId()))
                .count();

        if (place.getGeometry() != null) {
            placeLocation = new Location("");
            placeLocation.setLatitude(place.getGeometry().getLocation().getLat());
            placeLocation.setLongitude(place.getGeometry().getLocation().getLng());
        } else {
            placeLocation = null;
        }

        return new PlaceViewState(
                place.getId(),
                place.getName(),
                place.getVicinity(),
                place.getOpeningHours(),
                currentLocation,
                placeLocation,
                place.getRating(),
                photoReference,
                workmateCount
        );
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

    public LiveData<List<PlaceViewState>> getNearbyPlaces() {
        return formattedNearbyPlace;
    }

    public LiveData<Location> getCurrentLocation() {
        return locationRepository.getLocationLiveData();
    }

}
