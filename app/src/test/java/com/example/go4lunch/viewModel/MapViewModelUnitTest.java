package com.example.go4lunch.viewModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.never;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.PermissionChecker;
import com.example.go4lunch.data.models.firestore.User;
import com.example.go4lunch.data.models.map.Place;
import com.example.go4lunch.data.models.map.PlaceAutocompletePrediction;
import com.example.go4lunch.data.models.map.PlaceOpeningHours;
import com.example.go4lunch.data.services.GoogleMapRepository;
import com.example.go4lunch.data.services.LocationRepository;
import com.example.go4lunch.data.services.SearchRepository;
import com.example.go4lunch.data.services.UserRepository;
import com.example.go4lunch.ui.chat.ChatViewModel;
import com.example.go4lunch.ui.chat.MessageViewState;
import com.example.go4lunch.ui.map.MapViewModel;
import com.example.go4lunch.ui.map.PlaceViewState;
import com.example.go4lunch.utils.LiveDataTestUtil;
import com.example.go4lunch.utils.TimeUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


@RunWith(JUnit4.class)
public class MapViewModelUnitTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private GoogleMapRepository googleMapRepository;
    private LocationRepository locationRepository;
    private UserRepository userRepository;
    private PermissionChecker permissionChecker;
    private SearchRepository searchRepository;

    private static final User workmate1 = new User("1", "User 1", "user1@gmail.com", null);
    private static final User workmate2 = new User("2", "User 2", "user2@gmail.com", null);
    private static final User workmate3 = new User("3", "User 3", "user3@gmail.com", null);
    private Location currentLocation;

    @NonNull
    private List<User> getFakeWorkmates() {
        return new ArrayList<>(Arrays.asList(workmate1, workmate2, workmate3));
    }

    @NonNull
    private List<Place> getNearbyPlaces() {
        return new ArrayList<>(Arrays.asList(
            new Place(null,"14 rue test",null,null,
                    null,null,"Place 1", new PlaceOpeningHours(true,null,null,null,null),"place1",2F,null,null,null),
            new Place(null,"19 rue test",null,null,
                    null,null,"Restaurant Place 2",new PlaceOpeningHours(false,null,null,null,null),"place2",4F,null,null,null),
            new Place(null,"28 rue test",null,null,
                    null,null,"Restaurant Place 3",null,"place3",5F,null,null,null)
        ));
    }
    @NonNull
    private List<PlaceAutocompletePrediction> getPrediction() {
        return new ArrayList<>(Arrays.asList(
                new PlaceAutocompletePrediction("description place 2","place2"),
                new PlaceAutocompletePrediction("description place 3","place3")
        ));
    }

    @Before
    public void setup() {
        googleMapRepository = Mockito.mock(GoogleMapRepository.class);
        locationRepository = Mockito.mock(LocationRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        permissionChecker = Mockito.mock(PermissionChecker.class);
        searchRepository = Mockito.mock(SearchRepository.class);

        LiveData<List<PlaceAutocompletePrediction>> predictionLiveData = new MutableLiveData<>(getPrediction());
        LiveData<List<Place>> nearbyPlaceLiveData = new MutableLiveData<>(getNearbyPlaces());
        currentLocation = Mockito.mock();
        Mockito.when(currentLocation.getLatitude()).thenReturn(48.8588897);
        Mockito.when(currentLocation.getLongitude()).thenReturn(2.320041);
        LiveData<Location> locationLiveData = new MutableLiveData<>(currentLocation);
        LiveData<List<User>> workmatesLiveData = new MutableLiveData<>(getFakeWorkmates());
        LiveData<String> searchLiveData = new MutableLiveData<>("Restaurant");
        Mockito.when(googleMapRepository.getPlaceAutocomplete(any(),anyDouble(),anyDouble(),any()))
                .thenReturn(predictionLiveData);
        Mockito.when(googleMapRepository.getNearbyPlaceLiveData(anyDouble(),anyDouble()))
                .thenReturn(nearbyPlaceLiveData);
        Mockito.when(locationRepository.getLocationLiveData())
                .thenReturn(locationLiveData);
        Mockito.when(userRepository.getAllWorkmates())
                .thenReturn(workmatesLiveData);
        Mockito.when(searchRepository.getSearch())
                .thenReturn(searchLiveData);
    }

    @Test
    public void getCurrentLocation_success() throws InterruptedException {
        MapViewModel viewModel = new MapViewModel(googleMapRepository,locationRepository,userRepository,permissionChecker,searchRepository);
        Location result = LiveDataTestUtil.getOrAwaitValue(viewModel.getCurrentLocation());
        assertEquals(result,currentLocation);
    }

    @Test
    public void refreshWithPermission_success(){

        Mockito.when(permissionChecker.hasLocationPermission())
                .thenReturn(true);
        MapViewModel viewModel = new MapViewModel(googleMapRepository,locationRepository,userRepository,permissionChecker,searchRepository);
        viewModel.refresh();
        Mockito.verify(locationRepository).startLocationRequest();
        Mockito.verify(locationRepository,never()).stopLocationRequest();
    }

    @Test
    public void refreshWithoutPermission_success() {

        Mockito.when(permissionChecker.hasLocationPermission())
                .thenReturn(false);
        MapViewModel viewModel = new MapViewModel(googleMapRepository,locationRepository,userRepository,permissionChecker,searchRepository);
        viewModel.refresh();
        Mockito.verify(locationRepository).stopLocationRequest();
        Mockito.verify(locationRepository,never()).startLocationRequest();
    }

    @Test
    public void mapPlace_success() throws InterruptedException {
        Place place =  new Place(null,"14 rue test",null,null,
                null,null,"Place 1", new PlaceOpeningHours(true,null,null,null,null),"place1",2F,null,null,null);
        Map<String,Long> workmatesByPlace = new HashMap<>();
        workmatesByPlace.put("place1", 1L);
        workmatesByPlace.put("place2", 3L);
        PlaceViewState result = MapViewModel.mapPlace(place, workmatesByPlace);

        assertEquals(result.getPlaceId(),place.getId());
        assertEquals(result.getPlaceName(),place.getName());
        assertEquals(result.getWorkmateCount(),1L);
    }

}
