package com.example.go4lunch.viewModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.models.firestore.Place;
import com.example.go4lunch.data.models.firestore.User;
import com.example.go4lunch.data.models.map.MapPlace;
import com.example.go4lunch.data.services.FavoriteRepository;
import com.example.go4lunch.data.services.GoogleMapRepository;
import com.example.go4lunch.data.services.UserRepository;
import com.example.go4lunch.ui.placeDetail.PlaceDetailViewModel;
import com.example.go4lunch.ui.placeDetail.viewState.PlaceDetailViewState;
import com.example.go4lunch.ui.placeDetail.viewState.PlaceState;
import com.example.go4lunch.ui.placeDetail.viewState.UserState;
import com.example.go4lunch.ui.placeDetail.viewState.WorkmateState;
import com.example.go4lunch.utils.FakeDataUtil;
import com.example.go4lunch.utils.LiveDataTestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RunWith(JUnit4.class)
public class PlaceDetailViewModelUnitTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private final GoogleMapRepository googleMapRepositoryMock = mock();
    private final UserRepository userRepositoryMock = mock();
    private final FavoriteRepository favoriteRepositoryMock = mock();
    private final Place place = FakeDataUtil.getPlace();

    @Before
    public void setup() {
    }

    private void mockPlaceDetailsLiveData(MapPlace mapPlace) {
        LiveData<MapPlace> placeDetailsLiveData = new MutableLiveData<>(mapPlace);
        when(googleMapRepositoryMock.getPlaceDetailsLiveData(place.getUid(), null))
                .thenReturn(placeDetailsLiveData);
    }

    private void mockWorkmateLiveData(List<User> workmates) {
        LiveData<List<User>> workmatesLiveData = new MutableLiveData<>(workmates);
        when(userRepositoryMock.getWorkmatesForPlace(place.getUid()))
                .thenReturn(workmatesLiveData);
    }

    private void mockCurrentUserLiveData(User user) {
        LiveData<User> currentUserLiveData = new MutableLiveData<>(user);
        when(userRepositoryMock.getUserData())
                .thenReturn(currentUserLiveData);
    }

    private void mockIsLikeLiveData(Boolean isLike) {
        LiveData<Boolean> isLikeLiveData = new MutableLiveData<>(isLike);
        when(favoriteRepositoryMock.isLike(place.getUid()))
                .thenReturn(isLikeLiveData);
    }

    @Test
    public void mapPlaceData_success() {
        User user = FakeDataUtil.getFirestoreUser();

        WorkmateState result = PlaceDetailViewModel.mapUser(user);

        assertEquals(user.getUid(), result.getId());
        assertEquals(user.getDisplayName(), result.getWorkmateName());
        assertEquals(user.getUrlPicture(), result.getPicture());
    }

    @Test
    public void createPlaceDetailState_success() {
        MapPlace mapPlace = FakeDataUtil.getMapPlace();

        PlaceState result = PlaceDetailViewModel.createPlaceDetailState(mapPlace);

        assertEquals(mapPlace.getId(), result.getId());
        assertEquals(mapPlace.getName(), result.getName());
        assertEquals(mapPlace.getFormattedAddress(), result.getAddress());
        assertEquals(mapPlace.getFormattedPhoneNumber(), result.getPhone());
        assertEquals(mapPlace.getWebsite(), result.getWebsite());
    }

    @Test
    public void toggleLike_success() {

        mockPlaceDetailsLiveData(FakeDataUtil.getMapPlace());
        mockWorkmateLiveData(FakeDataUtil.getFirestoreUsersForPlace(3, place));
        mockCurrentUserLiveData(FakeDataUtil.getFirestoreUser());
        mockIsLikeLiveData(false);
        PlaceDetailViewModel viewModel = new PlaceDetailViewModel(googleMapRepositoryMock, userRepositoryMock, favoriteRepositoryMock, place.getUid());

        viewModel.toggleLike();

        verify(favoriteRepositoryMock).togglePlaceLike(place.getUid());
    }

    @Test
    public void getPlaceViewStateWithoutPlace_success() throws InterruptedException {

        mockPlaceDetailsLiveData(null);
        mockWorkmateLiveData(FakeDataUtil.getFirestoreUsersForPlace(3, place));
        mockCurrentUserLiveData(FakeDataUtil.getFirestoreUser());
        mockIsLikeLiveData(false);

        PlaceDetailViewModel viewModel = new PlaceDetailViewModel(googleMapRepositoryMock, userRepositoryMock, favoriteRepositoryMock, place.getUid());

        LiveData<PlaceDetailViewState> placeDetailViewStateLiveData = viewModel.getPlaceViewState();

        PlaceDetailViewState placeDetailViewState = LiveDataTestUtil.getOrAwaitValue(placeDetailViewStateLiveData);

        assertNull(placeDetailViewState);
    }

    @Test
    public void getPlaceViewStateWithoutUser_success() throws InterruptedException {

        mockPlaceDetailsLiveData(FakeDataUtil.getMapPlace());
        mockWorkmateLiveData(FakeDataUtil.getFirestoreUsersForPlace(3, place));
        mockCurrentUserLiveData(null);
        mockIsLikeLiveData(false);

        PlaceDetailViewModel viewModel = new PlaceDetailViewModel(googleMapRepositoryMock, userRepositoryMock, favoriteRepositoryMock, place.getUid());

        LiveData<PlaceDetailViewState> placeDetailViewStateLiveData = viewModel.getPlaceViewState();

        PlaceDetailViewState placeDetailViewState = LiveDataTestUtil.getOrAwaitValue(placeDetailViewStateLiveData);

        assertNull(placeDetailViewState);
    }

    @Test
    public void getPlaceViewStateAlreadyChosen_success() throws InterruptedException {

        List<User> fakeWorkmates = FakeDataUtil.getFirestoreUsersForPlace(3, place);
        User user = FakeDataUtil.getFirestoreUserWithPlace(place);
        boolean like = false;
        List<WorkmateState> workmates = fakeWorkmates.stream().map(PlaceDetailViewModel::mapUser).collect(Collectors.toList());
        MapPlace mapPlace = FakeDataUtil.getMapPlace();

        mockPlaceDetailsLiveData(mapPlace);
        mockWorkmateLiveData(fakeWorkmates);
        mockCurrentUserLiveData(user);
        mockIsLikeLiveData(like);

        PlaceDetailViewModel viewModel = new PlaceDetailViewModel(googleMapRepositoryMock, userRepositoryMock, favoriteRepositoryMock, place.getUid());

        LiveData<PlaceDetailViewState> placeDetailViewStateLiveData = viewModel.getPlaceViewState();

        PlaceDetailViewState expectedViewState = new PlaceDetailViewState(new UserState(user.getUid(), like, true), PlaceDetailViewModel.createPlaceDetailState(mapPlace), workmates);

        PlaceDetailViewState placeDetailViewState = LiveDataTestUtil.getOrAwaitValue(placeDetailViewStateLiveData);

        assertEquals(expectedViewState, placeDetailViewState);
    }

    @Test
    public void getPlaceViewStateNotChosen_success() throws InterruptedException {

        List<User> fakeWorkmates = FakeDataUtil.getFirestoreUsersForPlace(3, place);
        User user = FakeDataUtil.getFirestoreUser();
        boolean like = false;
        List<WorkmateState> workmates = fakeWorkmates.stream().map(PlaceDetailViewModel::mapUser).collect(Collectors.toList());
        MapPlace mapPlace = FakeDataUtil.getMapPlace();

        mockPlaceDetailsLiveData(mapPlace);
        mockWorkmateLiveData(fakeWorkmates);
        mockCurrentUserLiveData(user);
        mockIsLikeLiveData(like);

        PlaceDetailViewModel viewModel = new PlaceDetailViewModel(googleMapRepositoryMock, userRepositoryMock, favoriteRepositoryMock, place.getUid());

        LiveData<PlaceDetailViewState> placeDetailViewStateLiveData = viewModel.getPlaceViewState();

        PlaceDetailViewState expectedViewState = new PlaceDetailViewState(new UserState(user.getUid(), like, false), PlaceDetailViewModel.createPlaceDetailState(mapPlace), workmates);

        PlaceDetailViewState placeDetailViewState = LiveDataTestUtil.getOrAwaitValue(placeDetailViewStateLiveData);

        assertEquals(expectedViewState, placeDetailViewState);
    }

    @Test
    public void getPlaceViewStateNoWorkmate_success() throws InterruptedException {

        List<User> fakeWorkmates = new ArrayList<>();
        User user = FakeDataUtil.getFirestoreUser();
        boolean like = false;
        MapPlace mapPlace = FakeDataUtil.getMapPlace();

        mockPlaceDetailsLiveData(mapPlace);
        mockWorkmateLiveData(fakeWorkmates);
        mockCurrentUserLiveData(user);
        mockIsLikeLiveData(like);

        PlaceDetailViewModel viewModel = new PlaceDetailViewModel(googleMapRepositoryMock, userRepositoryMock, favoriteRepositoryMock, place.getUid());

        LiveData<PlaceDetailViewState> placeDetailViewStateLiveData = viewModel.getPlaceViewState();

        PlaceDetailViewState expectedViewState = new PlaceDetailViewState(new UserState(user.getUid(), like, false), PlaceDetailViewModel.createPlaceDetailState(mapPlace), new ArrayList<>());

        PlaceDetailViewState placeDetailViewState = LiveDataTestUtil.getOrAwaitValue(placeDetailViewStateLiveData);

        assertEquals(expectedViewState, placeDetailViewState);
        assertEquals(0, placeDetailViewState.getWorkmates().size());
    }

}
