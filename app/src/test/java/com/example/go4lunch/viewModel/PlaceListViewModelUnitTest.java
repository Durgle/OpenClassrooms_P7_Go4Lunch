package com.example.go4lunch.viewModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.location.Location;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.R;
import com.example.go4lunch.data.PermissionChecker;
import com.example.go4lunch.data.models.firestore.User;
import com.example.go4lunch.data.models.map.MapPlace;
import com.example.go4lunch.data.models.map.PlaceAutocompletePrediction;
import com.example.go4lunch.data.services.GoogleMapRepository;
import com.example.go4lunch.data.services.LocationRepository;
import com.example.go4lunch.data.services.SearchRepository;
import com.example.go4lunch.data.services.UserRepository;
import com.example.go4lunch.ui.placeList.PlaceListViewModel;
import com.example.go4lunch.ui.placeList.PlaceViewState;
import com.example.go4lunch.utils.FakeDataUtil;
import com.example.go4lunch.utils.GoogleMapApiUtils;
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
public class PlaceListViewModelUnitTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private final GoogleMapRepository googleMapRepositoryMock = mock();
    private final LocationRepository locationRepositoryMock = mock();
    private final UserRepository userRepositoryMock = mock();
    private final PermissionChecker permissionCheckerMock = mock();
    private final SearchRepository searchRepositoryMock = mock();

    @Before
    public void setup() {
    }

    private void mockPredictionLiveData(List<PlaceAutocompletePrediction> predictions) {
        LiveData<List<PlaceAutocompletePrediction>> predictionLiveData = new MutableLiveData<>(predictions);
        when(googleMapRepositoryMock.getPlaceAutocomplete(any(), anyDouble(), anyDouble(), any()))
                .thenReturn(predictionLiveData);
    }

    private void mockMapPlaceLiveData(List<MapPlace> mapPlaces) {
        LiveData<List<MapPlace>> nearbyPlaceLiveData = new MutableLiveData<>(mapPlaces);
        when(googleMapRepositoryMock.getNearbyPlaceLiveData(anyDouble(), anyDouble()))
                .thenReturn(nearbyPlaceLiveData);
    }

    private void mockCurrentLocationLiveData(Location currentLocation) {
        LiveData<Location> locationLiveData = new MutableLiveData<>(currentLocation);
        when(locationRepositoryMock.getLocationLiveData())
                .thenReturn(locationLiveData);
    }

    private void mockWorkmatesLiveData(List<User> workmates) {
        LiveData<List<User>> workmatesLiveData = new MutableLiveData<>(workmates);
        when(userRepositoryMock.getAllWorkmates())
                .thenReturn(workmatesLiveData);
    }

    private void mockSearchLiveData(String search) {
        LiveData<String> searchLiveData = new MutableLiveData<>(search);
        when(searchRepositoryMock.getSearch())
                .thenReturn(searchLiveData);
    }

    private void mockCreateLocation(Location placeLocation) {
        when(locationRepositoryMock.createLocation(anyDouble(),anyDouble())).thenReturn(placeLocation);
    }

    @Test
    public void mapPlaceData_success() {
        MapPlace place = FakeDataUtil.getMapPlace();
        Location location = FakeDataUtil.getLocation(1.1,2.0);
        assert place.getGeometry() != null;
        Location placeLocation = FakeDataUtil.getLocation(place.getGeometry().getLocation().getLat(),place.getGeometry().getLocation().getLng());
        when(locationRepositoryMock.createLocation(anyDouble(),anyDouble())).thenReturn(placeLocation);
        when(location.distanceTo(any())).thenReturn(5.0F);
        List<User> workmates = FakeDataUtil.getFirestoreUsersWithPlace();

        PlaceViewState result = PlaceListViewModel.mapPlaceData(place, placeLocation, location, workmates);

        assertEquals(place.getId(),result.getId());
        assertEquals(place.getName(), result.getName());
        assertEquals(place.getVicinity(),result.getAddress());
        Integer statusExpected = R.string.place_status_close;
        assertEquals(statusExpected,result.getStatus());
        assertEquals(GoogleMapApiUtils.formatStarRating(place.getRating()),result.getRating());
        assertEquals(2,result.getWorkmateCount());
        assertEquals("5m",result.getDistance());
    }

    @Test
    public void refreshWithPermission_success() {
        mockCurrentLocationLiveData(FakeDataUtil.getLocation(48.8588897, 2.320041));
        mockSearchLiveData("Restaurant");
        mockPredictionLiveData(FakeDataUtil.getPlacePredictions());
        mockMapPlaceLiveData(FakeDataUtil.getNearbyPlaces());
        mockWorkmatesLiveData(FakeDataUtil.getFirestoreUsersWithPlace());
        mockCreateLocation(FakeDataUtil.getLocation(1.0,2.0));

        when(permissionCheckerMock.hasLocationPermission())
                .thenReturn(true);
        PlaceListViewModel viewModel = new PlaceListViewModel(googleMapRepositoryMock, locationRepositoryMock, permissionCheckerMock, userRepositoryMock, searchRepositoryMock);
        viewModel.refresh();
        verify(locationRepositoryMock).startLocationRequest();
        verify(locationRepositoryMock, never()).stopLocationRequest();
    }

    @Test
    public void refreshWithoutPermission_success() {
        mockCurrentLocationLiveData(FakeDataUtil.getLocation(48.8588897, 2.320041));
        mockSearchLiveData("Restaurant");
        mockPredictionLiveData(FakeDataUtil.getPlacePredictions());
        mockMapPlaceLiveData(FakeDataUtil.getNearbyPlaces());
        mockWorkmatesLiveData(FakeDataUtil.getFirestoreUsersWithPlace());
        mockCreateLocation(FakeDataUtil.getLocation(1.0,2.0));

        when(permissionCheckerMock.hasLocationPermission())
                .thenReturn(false);
        PlaceListViewModel viewModel = new PlaceListViewModel(googleMapRepositoryMock, locationRepositoryMock, permissionCheckerMock, userRepositoryMock, searchRepositoryMock);
        viewModel.refresh();
        verify(locationRepositoryMock).stopLocationRequest();
        verify(locationRepositoryMock, never()).startLocationRequest();
    }

    @Test
    public void getCurrentLocation_success() throws InterruptedException {
        Location currentLocation = FakeDataUtil.getLocation(48.8588897, 2.320041);
        mockCurrentLocationLiveData(currentLocation);
        mockSearchLiveData("Restaurant");
        mockPredictionLiveData(FakeDataUtil.getPlacePredictions());
        mockMapPlaceLiveData(FakeDataUtil.getNearbyPlaces());
        mockWorkmatesLiveData(FakeDataUtil.getFirestoreUsersWithPlace());
        mockCreateLocation(FakeDataUtil.getLocation(1.0,2.0));

        PlaceListViewModel viewModel = new PlaceListViewModel(googleMapRepositoryMock, locationRepositoryMock, permissionCheckerMock, userRepositoryMock, searchRepositoryMock);
        Location result = LiveDataTestUtil.getOrAwaitValue(viewModel.getCurrentLocation());
        assertEquals(result, currentLocation);
    }

    @Test
    public void getNearbyPlacesWithoutLocation_success() throws InterruptedException {
        mockCurrentLocationLiveData(null);
        mockSearchLiveData(null);
        mockPredictionLiveData(null);
        mockMapPlaceLiveData(FakeDataUtil.getNearbyPlaces());
        mockWorkmatesLiveData(FakeDataUtil.getFirestoreUsersWithPlace());
        mockCreateLocation(FakeDataUtil.getLocation(1.0,2.0));

        PlaceListViewModel viewModel = new PlaceListViewModel(googleMapRepositoryMock, locationRepositoryMock, permissionCheckerMock, userRepositoryMock, searchRepositoryMock);

        LiveData<List<PlaceViewState>> nearbyPlaceLiveData = viewModel.getNearbyPlaces();

        List<PlaceViewState> nearbyPlace = LiveDataTestUtil.getOrAwaitValue(nearbyPlaceLiveData);

        assertTrue(nearbyPlace.isEmpty());
    }

    @Test
    public void getNearbyPlacesWithoutPlace_success() throws InterruptedException {
        mockCurrentLocationLiveData(FakeDataUtil.getLocation(48.8588897, 2.320041));
        mockSearchLiveData(null);
        mockPredictionLiveData(null);
        mockMapPlaceLiveData(null);
        mockWorkmatesLiveData(FakeDataUtil.getFirestoreUsersWithPlace());
        mockCreateLocation(FakeDataUtil.getLocation(1.0,2.0));

        PlaceListViewModel viewModel = new PlaceListViewModel(googleMapRepositoryMock, locationRepositoryMock, permissionCheckerMock, userRepositoryMock, searchRepositoryMock);

        LiveData<List<PlaceViewState>> nearbyPlaceLiveData = viewModel.getNearbyPlaces();

        List<PlaceViewState> nearbyPlace = LiveDataTestUtil.getOrAwaitValue(nearbyPlaceLiveData);

        assertTrue(nearbyPlace.isEmpty());
    }

    @Test
    public void getNearbyPlacesWithoutWorkmate_success() throws InterruptedException {
        mockCurrentLocationLiveData(FakeDataUtil.getLocation(48.8588897, 2.320041));
        mockSearchLiveData(null);
        mockPredictionLiveData(null);
        List<MapPlace> nearbyPlace = FakeDataUtil.getNearbyPlaces();
        mockMapPlaceLiveData(nearbyPlace);
        mockWorkmatesLiveData(new ArrayList<>());
        mockCreateLocation(FakeDataUtil.getLocation(1.0,2.0));

        PlaceListViewModel viewModel = new PlaceListViewModel(googleMapRepositoryMock, locationRepositoryMock, permissionCheckerMock, userRepositoryMock, searchRepositoryMock);

        LiveData<List<PlaceViewState>> nearbyPlaceLiveData = viewModel.getNearbyPlaces();

        List<PlaceViewState> nearbyPlaceViewState = LiveDataTestUtil.getOrAwaitValue(nearbyPlaceLiveData);

        assertFalse(nearbyPlaceViewState.isEmpty());
        assertEquals(nearbyPlace.size(), nearbyPlaceViewState.size());

        for (PlaceViewState placeViewState : nearbyPlaceViewState) {
            assertEquals(0L, placeViewState.getWorkmateCount());
        }
    }

    @Test
    public void getNearbyPlaces_success() throws InterruptedException {
        mockCurrentLocationLiveData(FakeDataUtil.getLocation(48.8588897, 2.320041));
        mockSearchLiveData(null);
        mockPredictionLiveData(null);
        List<MapPlace> nearbyPlace = FakeDataUtil.getNearbyPlaces();
        mockMapPlaceLiveData(nearbyPlace);
        mockWorkmatesLiveData(FakeDataUtil.getFirestoreUsersWithPlace());
        mockCreateLocation(FakeDataUtil.getLocation(1.0,2.0));

        PlaceListViewModel viewModel = new PlaceListViewModel(googleMapRepositoryMock, locationRepositoryMock, permissionCheckerMock, userRepositoryMock, searchRepositoryMock);

        LiveData<List<PlaceViewState>> nearbyPlaceLiveData = viewModel.getNearbyPlaces();

        List<PlaceViewState> nearbyPlaceViewState = LiveDataTestUtil.getOrAwaitValue(nearbyPlaceLiveData);

        assertFalse(nearbyPlaceViewState.isEmpty());
        assertEquals(nearbyPlace.size(), nearbyPlaceViewState.size());

        for (PlaceViewState placeViewState : nearbyPlaceViewState) {
            if(placeViewState.getId().equals("place1")){
                assertEquals(2L, placeViewState.getWorkmateCount());
            }else if(placeViewState.getId().equals("place2")){
                assertEquals(1L, placeViewState.getWorkmateCount());
            } else {
                assertEquals(0L, placeViewState.getWorkmateCount());
            }

        }
    }

    @Test
    public void getNearbyPlacesWithSearch_success() throws InterruptedException {
        mockCurrentLocationLiveData(FakeDataUtil.getLocation(48.8588897, 2.320041));
        mockSearchLiveData("restaurant");
        List<PlaceAutocompletePrediction> predictions = FakeDataUtil.getPlacePredictions();
        mockPredictionLiveData(predictions);
        List<MapPlace> nearbyPlace = FakeDataUtil.getNearbyPlaces();
        mockMapPlaceLiveData(nearbyPlace);
        mockWorkmatesLiveData(FakeDataUtil.getFirestoreUsersWithPlace());
        mockCreateLocation(FakeDataUtil.getLocation(1.0,2.0));

        PlaceListViewModel viewModel = new PlaceListViewModel(googleMapRepositoryMock, locationRepositoryMock, permissionCheckerMock, userRepositoryMock, searchRepositoryMock);

        LiveData<List<PlaceViewState>> nearbyPlaceLiveData = viewModel.getNearbyPlaces();

        LiveDataTestUtil.getOrAwaitValue(nearbyPlaceLiveData);
        List<PlaceViewState> nearbyPlaceViewState = LiveDataTestUtil.getOrAwaitValue(nearbyPlaceLiveData);

        assertTrue(nearbyPlaceViewState.size() <= nearbyPlace.size());

        List<String> allowedIds = predictions.stream()
                .map(PlaceAutocompletePrediction::getPlaceId)
                .collect(Collectors.toList());

        for (PlaceViewState placeViewState : nearbyPlaceViewState) {
            assertTrue(allowedIds.contains(placeViewState.getId()));
        }
    }

}
