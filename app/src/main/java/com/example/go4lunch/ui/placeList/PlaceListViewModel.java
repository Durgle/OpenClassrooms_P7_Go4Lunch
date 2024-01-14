package com.example.go4lunch.ui.placeList;

import android.annotation.SuppressLint;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.PermissionChecker;
import com.example.go4lunch.data.models.map.Place;
import com.example.go4lunch.data.services.GoogleMapRepository;
import com.example.go4lunch.data.services.LocationRepository;

import java.util.ArrayList;
import java.util.List;

public class PlaceListViewModel extends ViewModel {

    private final GoogleMapRepository googleMapRepository;
    private final LocationRepository locationRepository;
    private final PermissionChecker permissionChecker;
    private final LiveData<List<Place>> nearbyPlace;

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

    public LiveData<List<Place>> getNearbyPlaces(){
        return nearbyPlace;
    }

    public LiveData<Location> getCurrentLocation(){
        return locationRepository.getLocationLiveData();
    }

}
