package com.example.go4lunch.ui.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.models.map.Place;
import com.example.go4lunch.data.services.GoogleMapRepository;

import java.util.List;

public class MapViewModel extends ViewModel {

    private final GoogleMapRepository mGoogleMapRepository;

    public MapViewModel(GoogleMapRepository googleMapRepository) {
        mGoogleMapRepository = googleMapRepository;

    }

    public LiveData<List<Place>> getPlaces(){
        return mGoogleMapRepository.getNearbyPlaceLiveData(48.8731223, 2.3468543);
    }
}
