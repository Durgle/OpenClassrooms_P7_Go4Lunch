package com.example.go4lunch.ui.placeList;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.PermissionChecker;
import com.example.go4lunch.data.models.map.Place;
import com.example.go4lunch.data.models.map.PlaceOpeningHours;
import com.example.go4lunch.data.models.map.PlacePhoto;
import com.example.go4lunch.data.services.GoogleMapRepository;
import com.example.go4lunch.data.services.LocationRepository;

import java.util.ArrayList;
import java.util.List;

public class PlaceListViewModel extends ViewModel {

    private final GoogleMapRepository googleMapRepository;
    private final LocationRepository locationRepository;
    private final PermissionChecker permissionChecker;
    private final LiveData<List<Place>> nearbyPlace;
    private final MediatorLiveData<List<PlaceViewState>> formattedNearbyPlace = new MediatorLiveData<>();

    public PlaceListViewModel(GoogleMapRepository googleMapRepository, LocationRepository locationRepository, PermissionChecker permissionChecker) {
        this.googleMapRepository = googleMapRepository;
        this.locationRepository = locationRepository;
        this.permissionChecker = permissionChecker;

        LiveData<Location> locationLiveData = locationRepository.getLocationLiveData();

        nearbyPlace = Transformations.switchMap(locationLiveData, location -> {
            if(location != null){
                return this.googleMapRepository.getNearbyPlaceLiveData(location.getLatitude(), location.getLongitude());
            }
            return new MutableLiveData<>(new ArrayList<>());
        });

        formattedNearbyPlace.addSource(nearbyPlace, placeList -> combine(placeList,locationLiveData.getValue()));

    }

    private void combine(@Nullable List<Place> placeList, @Nullable Location currentLocation) {

        if(placeList != null && currentLocation != null){
            List<PlaceViewState> formattedList = new ArrayList<>();
            for (Place place : placeList) {
                formattedList.add(mapPlaceData(place,currentLocation));
            }
            formattedNearbyPlace.setValue(formattedList);
        } else {
            formattedNearbyPlace.setValue(new ArrayList<>());
        }
    }

    private PlaceViewState mapPlaceData(@NonNull Place place,@NonNull Location currentLocation){

        String photoReference = (place.getPhotos() != null) ? place.getPhotos().get(0).getPhotoReference() : null;
        Double latitude;
        Double longitude;

        if(place.getGeometry() != null){
            latitude = place.getGeometry().getLocation().getLat();
            longitude = place.getGeometry().getLocation().getLng();
        } else {
            latitude = null;
            longitude = null;
        }

        return new PlaceViewState(
                place.getId(),
                place.getName(),
                place.getVicinity(),
                place.getOpeningHours(),
                currentLocation.getLatitude(),
                currentLocation.getLongitude(),
                latitude,
                longitude,
                place.getRating(),
                photoReference
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

    public LiveData<List<PlaceViewState>> getNearbyPlaces(){
        return formattedNearbyPlace;
    }

    public LiveData<Location> getCurrentLocation(){
        return locationRepository.getLocationLiveData();
    }

}
