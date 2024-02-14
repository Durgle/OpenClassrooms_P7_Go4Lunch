package com.example.go4lunch.ui.placeDetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.models.firestore.User;
import com.example.go4lunch.data.models.map.Place;
import com.example.go4lunch.data.models.map.PlacePhoto;
import com.example.go4lunch.data.services.GoogleMapRepository;
import com.example.go4lunch.data.services.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PlaceDetailViewModel extends ViewModel {

    private final GoogleMapRepository googleMapRepository;
    private final UserRepository userRepository;
    private final String placeId;
    private final LiveData<PlaceViewState> place;
    private final LiveData<List<WorkmateViewState>> workmateList;

    public static WorkmateViewState mapUser(User user){
        return new WorkmateViewState(
                user.getUid(),
                user.getDisplayName(),
                user.getUrlPicture()
        );
    }

    public PlaceDetailViewModel(GoogleMapRepository googleMapRepository, UserRepository userRepository,String placeId) {
        this.googleMapRepository = googleMapRepository;
        this.userRepository = userRepository;
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

        this.workmateList = Transformations.map(userRepository.getUserForPlace(placeId), userList -> {
            List<WorkmateViewState> list = new ArrayList<>();
            for (User user : userList) {
                list.add(mapUser(user));
            }
            return list;
        });

    }

    public LiveData<PlaceViewState> getPlace() {
        return this.place;
    }

    public LiveData<List<WorkmateViewState>> getWorkmateJoining() {
        return workmateList;
    }

}
