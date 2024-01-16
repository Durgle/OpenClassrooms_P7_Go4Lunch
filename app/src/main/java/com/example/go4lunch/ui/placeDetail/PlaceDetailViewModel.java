package com.example.go4lunch.ui.placeDetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.models.map.Place;
import com.example.go4lunch.data.models.map.PlacePhoto;
import com.example.go4lunch.data.services.GoogleMapRepository;

import java.util.function.Function;

public class PlaceDetailViewModel extends ViewModel {

    private final GoogleMapRepository googleMapRepository;
    private final String placeId;
    private LiveData<PlaceViewState> place;

    public PlaceDetailViewModel(GoogleMapRepository googleMapRepository,String placeId) {
        this.googleMapRepository = googleMapRepository;
        this.placeId = placeId;
        LiveData<Place> placeDetailsLiveData = googleMapRepository.getPlaceDetailsLiveData(this.placeId, null);

        this.place = Transformations.map(placeDetailsLiveData, place -> {

            String photoReference = (place.getPhotos() != null) ? place.getPhotos().get(0).getPhotoReference() : null;

            return new PlaceViewState(
                    place.getId(),
                    place.getName(),
                    place.getFormattedAddress(),
                    place.getOpeningHours(),
                    place.getFormattedPhoneNumber(),
                    place.getRating(),
                    false,
                    place.getWebsite(),
                    photoReference

            );
        });

    }

    public LiveData<PlaceViewState> getPlace() {
        return this.place;
    }

}
