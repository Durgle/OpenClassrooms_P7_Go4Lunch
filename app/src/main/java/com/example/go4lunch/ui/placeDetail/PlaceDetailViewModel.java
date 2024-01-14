package com.example.go4lunch.ui.placeDetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.models.map.Place;
import com.example.go4lunch.data.services.GoogleMapRepository;

public class PlaceDetailViewModel extends ViewModel {

    private final GoogleMapRepository googleMapRepository;
    private final String placeId;
    private LiveData<Place> place;

    public PlaceDetailViewModel(GoogleMapRepository googleMapRepository,String placeId) {
        this.googleMapRepository = googleMapRepository;
        this.placeId = placeId;
        this.place = googleMapRepository.getPlaceDetailsLiveData(this.placeId,null);
    }

    public LiveData<Place> getPlace() {
        return this.place;
    }

}
