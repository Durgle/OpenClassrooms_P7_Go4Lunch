package com.example.go4lunch.injection;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.SavedStateHandleSupport;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.CreationExtras;

import com.example.go4lunch.MainApplication;
import com.example.go4lunch.data.PermissionChecker;
import com.example.go4lunch.data.RetrofitService;
import com.example.go4lunch.data.services.FavoriteRepository;
import com.example.go4lunch.data.services.GoogleMapRepository;
import com.example.go4lunch.data.services.LocationRepository;
import com.example.go4lunch.data.services.SearchRepository;
import com.example.go4lunch.data.services.UserRepository;
import com.example.go4lunch.ui.AppViewModel;
import com.example.go4lunch.ui.AuthViewModel;
import com.example.go4lunch.ui.map.MapViewModel;
import com.example.go4lunch.ui.placeDetail.PlaceDetailActivity;
import com.example.go4lunch.ui.placeDetail.PlaceDetailViewModel;
import com.example.go4lunch.ui.placeList.PlaceListViewModel;
import com.example.go4lunch.ui.workmate.WorkmateViewModel;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private static ViewModelFactory factory;

    @NonNull
    private final GoogleMapRepository googleMapRepository;

    @NonNull
    private final PermissionChecker permissionChecker;

    @NonNull
    private final LocationRepository locationRepository;

    @NonNull
    private final UserRepository userRepository;

    @NonNull
    private final FavoriteRepository favoriteRepository;

    @NonNull
    private final SearchRepository searchRepository;

    public static ViewModelFactory getInstance() {
        if (factory == null) {
            synchronized (ViewModelFactory.class) {
                if (factory == null) {
                    Application application = MainApplication.getApplication();
                    factory = new ViewModelFactory(
                            new GoogleMapRepository(RetrofitService.getGoogleMapApi()),
                            new PermissionChecker(application),
                            new LocationRepository(
                                    LocationServices.getFusedLocationProviderClient(application)
                            ),
                            new UserRepository(FirebaseFirestore.getInstance(), AuthUI.getInstance(), FirebaseAuth.getInstance()),
                            new FavoriteRepository(FirebaseFirestore.getInstance(), AuthUI.getInstance(), FirebaseAuth.getInstance()),
                            new SearchRepository()
                    );
                }
            }
        }
        return factory;
    }

    private ViewModelFactory(
            @NonNull GoogleMapRepository googleMapRepository,
            @NonNull PermissionChecker permissionChecker,
            @NonNull LocationRepository locationRepository,
            @NonNull UserRepository userRepository,
            @NonNull FavoriteRepository favoriteRepository,
            @NonNull SearchRepository searchRepository
    ) {
        this.googleMapRepository = googleMapRepository;
        this.permissionChecker = permissionChecker;
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
        this.favoriteRepository = favoriteRepository;
        this.searchRepository = searchRepository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass, @NonNull CreationExtras extras) {
        if (modelClass.isAssignableFrom(MapViewModel.class)) {
            return (T) new MapViewModel(
                    this.googleMapRepository,
                    this.locationRepository,
                    this.userRepository,
                    this.permissionChecker,
                    this.searchRepository
            );
        }
        if (modelClass.isAssignableFrom(PlaceListViewModel.class)) {
            return (T) new PlaceListViewModel(
                    this.googleMapRepository,
                    this.locationRepository,
                    this.permissionChecker,
                    this.userRepository,
                    this.searchRepository
            );
        }
        if (modelClass.isAssignableFrom(PlaceDetailViewModel.class)) {
            SavedStateHandle savedStateHandle = SavedStateHandleSupport.createSavedStateHandle(extras);
            String placeId = savedStateHandle.get(PlaceDetailActivity.PLACE_ID);
            return (T) new PlaceDetailViewModel(
                    this.googleMapRepository,
                    this.userRepository,
                    this.favoriteRepository,
                    placeId
            );
        }
        if (modelClass.isAssignableFrom(AuthViewModel.class)) {
            return (T) new AuthViewModel(this.userRepository);
        }
        if (modelClass.isAssignableFrom(AppViewModel.class)) {
            return (T) new AppViewModel(this.userRepository, this.searchRepository);
        }
        if (modelClass.isAssignableFrom(WorkmateViewModel.class)) {
            return (T) new WorkmateViewModel(this.userRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}