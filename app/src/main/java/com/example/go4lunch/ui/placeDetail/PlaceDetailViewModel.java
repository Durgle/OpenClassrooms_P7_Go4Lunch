package com.example.go4lunch.ui.placeDetail;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.models.firestore.User;
import com.example.go4lunch.data.models.map.Place;
import com.example.go4lunch.data.services.FavoriteRepository;
import com.example.go4lunch.data.services.GoogleMapRepository;
import com.example.go4lunch.data.services.UserRepository;
import com.example.go4lunch.ui.placeDetail.viewState.PlaceDetailViewState;
import com.example.go4lunch.ui.placeDetail.viewState.PlaceState;
import com.example.go4lunch.ui.placeDetail.viewState.UserState;
import com.example.go4lunch.ui.placeDetail.viewState.WorkmateState;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PlaceDetailViewModel extends ViewModel {

    private final GoogleMapRepository googleMapRepository;
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;
    private final String placeId;
    private final MediatorLiveData<PlaceDetailViewState> placeDetailViewState = new MediatorLiveData<>();

    @NonNull
    public static WorkmateState mapUser(@NonNull User user) {
        return new WorkmateState(
                user.getUid(),
                user.getDisplayName(),
                user.getUrlPicture()
        );
    }

    public PlaceDetailViewModel(
            GoogleMapRepository googleMapRepository,
            UserRepository userRepository,
            FavoriteRepository favoriteRepository,
            String placeId
    ) {
        this.googleMapRepository = googleMapRepository;
        this.userRepository = userRepository;
        this.favoriteRepository = favoriteRepository;
        this.placeId = placeId;
        LiveData<Place> placeDetailsLiveData = googleMapRepository.getPlaceDetailsLiveData(this.placeId, null);
        LiveData<List<User>> workmateListLiveData = userRepository.getWorkmatesForPlace(placeId);
        LiveData<User> userLiveData = userRepository.getUserData();
        LiveData<Boolean> isLikeLiveData = favoriteRepository.isLike(placeId);

        placeDetailViewState.addSource(
                placeDetailsLiveData, placeDetail -> combine(
                        placeDetail,
                        workmateListLiveData.getValue(),
                        userLiveData.getValue(),
                        isLikeLiveData.getValue()
                )
        );
        placeDetailViewState.addSource(
                workmateListLiveData, workmateList -> combine(
                        placeDetailsLiveData.getValue(),
                        workmateList,
                        userLiveData.getValue(),
                        isLikeLiveData.getValue()
                )
        );
        placeDetailViewState.addSource(
                userLiveData, user -> combine(
                        placeDetailsLiveData.getValue(),
                        workmateListLiveData.getValue(),
                        user,
                        isLikeLiveData.getValue()
                )
        );
        placeDetailViewState.addSource(
                isLikeLiveData, isLike -> combine(
                        placeDetailsLiveData.getValue(),
                        workmateListLiveData.getValue(),
                        userLiveData.getValue(),
                        isLike
                )
        );
    }

    private void combine(
            @Nullable Place place,
            @Nullable List<User> workmateList,
            @Nullable User user,
            @Nullable Boolean favorite
    ) {

        if (place != null && workmateList != null && user != null && favorite != null) {
            Boolean placeChosen = user.getPlace() != null && Objects.equals(user.getPlace().getUid(), this.placeId);
            UserState userData = new UserState(user.getUid(), favorite, placeChosen);
            PlaceState placeDetail = createPlaceDetailState(place);
            List<WorkmateState> workmatelist = workmateList.stream().map(PlaceDetailViewModel::mapUser).collect(Collectors.toList());
            PlaceDetailViewState viewState = new PlaceDetailViewState(userData, placeDetail, workmatelist);
            placeDetailViewState.setValue(viewState);
        } else {
            placeDetailViewState.setValue(null);
        }

    }

    @NonNull
    private PlaceState createPlaceDetailState(@NonNull Place place) {
        return new PlaceState(
                place.getId(),
                place.getName(),
                place.getFormattedAddress(),
                place.getFormattedPhoneNumber(),
                place.getRating(),
                place.getWebsite(),
                (place.getPhotos() != null) ? place.getPhotos().get(0).getPhotoReference() : null
        );
    }

    public LiveData<PlaceDetailViewState> getPlaceViewState() {
        return this.placeDetailViewState;
    }

    public void toggleLike() {
        this.favoriteRepository.togglePlaceLike(placeId);
    }

    public void chooseRestaurant(String placeId, String placeName) {
        PlaceDetailViewState viewState = placeDetailViewState.getValue();
        if (viewState != null) {
            if (viewState.getUser().isChoose()) {
                this.userRepository.updateChosenRestaurant(null);
            } else {
                this.userRepository.updateChosenRestaurant(
                        new com.example.go4lunch.data.models.firestore.Place(placeId, placeName)
                );
            }
        }

    }

}
