package com.example.go4lunch.injection;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.CreationExtras;

import com.example.go4lunch.MainApplication;
import com.example.go4lunch.data.PermissionChecker;
import com.example.go4lunch.data.RetrofitService;
import com.example.go4lunch.data.services.GoogleMapRepository;
import com.example.go4lunch.data.services.LocationRepository;
import com.example.go4lunch.ui.map.MapViewModel;
import com.google.android.gms.location.LocationServices;

import org.jetbrains.annotations.NotNull;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private static ViewModelFactory factory;

    @NonNull
    private final PermissionChecker mPermissionChecker;

    @NonNull
    private final LocationRepository mLocationRepository;

    public static ViewModelFactory getInstance() {
        if (factory == null) {
            synchronized (ViewModelFactory.class) {
                if (factory == null) {
                    Application application = MainApplication.getApplication();
                    factory = new ViewModelFactory(
                        new PermissionChecker(
                                application
                        ),
                        new LocationRepository(
                            LocationServices.getFusedLocationProviderClient(
                                    application
                            )
                        )
                    );
                }
            }
        }
        return factory;
    }

    private final GoogleMapRepository googleMapRepository = new GoogleMapRepository(
            RetrofitService.getGoogleMapApi()
    );

    private ViewModelFactory(
            @NonNull PermissionChecker permissionChecker,
            @NonNull LocationRepository locationRepository
    ) {
        mPermissionChecker = permissionChecker;
        mLocationRepository = locationRepository;
    }

    @SuppressWarnings("unchecked")
    @Override
    @NotNull
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MapViewModel.class)) {
            // We inject the Repository in the ViewModel constructor
            return (T) new MapViewModel(
                    googleMapRepository,
                    mLocationRepository,
                    mPermissionChecker
            );
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass, @NonNull CreationExtras extras) {
        return ViewModelProvider.Factory.super.create(modelClass, extras);
    }
}