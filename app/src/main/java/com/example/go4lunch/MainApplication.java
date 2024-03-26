package com.example.go4lunch;

import android.app.Application;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.example.go4lunch.data.PermissionChecker;
import com.example.go4lunch.data.RetrofitService;
import com.example.go4lunch.data.services.ChatRepository;
import com.example.go4lunch.data.services.FavoriteRepository;
import com.example.go4lunch.data.services.GoogleMapRepository;
import com.example.go4lunch.data.services.LocationRepository;
import com.example.go4lunch.data.services.SearchRepository;
import com.example.go4lunch.data.services.SettingRepository;
import com.example.go4lunch.data.services.UserRepository;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainApplication extends Application {

    private static MainApplication sApplication;
    /**
     * @noinspection NotNullFieldNotInitialized
     */
    @NonNull
    private GoogleMapRepository googleMapRepository;
    /**
     * @noinspection NotNullFieldNotInitialized
     */
    @NonNull
    private PermissionChecker permissionChecker;
    /**
     * @noinspection NotNullFieldNotInitialized
     */
    @NonNull
    private LocationRepository locationRepository;
    /**
     * @noinspection NotNullFieldNotInitialized
     */
    @NonNull
    private UserRepository userRepository;
    /**
     * @noinspection NotNullFieldNotInitialized
     */
    @NonNull
    private FavoriteRepository favoriteRepository;
    /**
     * @noinspection NotNullFieldNotInitialized
     */
    @NonNull
    private SearchRepository searchRepository;
    /**
     * @noinspection NotNullFieldNotInitialized
     */
    @NonNull
    private ChatRepository chatRepository;
    /**
     * @noinspection NotNullFieldNotInitialized
     */
    @NonNull
    private SettingRepository settingRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        sApplication = this;
        googleMapRepository = new GoogleMapRepository(RetrofitService.getGoogleMapApi());
        permissionChecker = new PermissionChecker(this);
        locationRepository = new LocationRepository(LocationServices.getFusedLocationProviderClient(this), Looper.getMainLooper());
        userRepository = new UserRepository(FirebaseFirestore.getInstance(), AuthUI.getInstance(), FirebaseAuth.getInstance());
        favoriteRepository = new FavoriteRepository(FirebaseFirestore.getInstance(), FirebaseAuth.getInstance());
        searchRepository = new SearchRepository();
        chatRepository = new ChatRepository(FirebaseFirestore.getInstance(), FirebaseAuth.getInstance());
        settingRepository = new SettingRepository(FirebaseFirestore.getInstance(), FirebaseAuth.getInstance());
    }

    public static MainApplication getApplication() {
        return sApplication;
    }

    @NonNull
    public GoogleMapRepository getGoogleMapRepository() {
        return googleMapRepository;
    }

    @NonNull
    public PermissionChecker getPermissionChecker() {
        return permissionChecker;
    }

    @NonNull
    public LocationRepository getLocationRepository() {
        return locationRepository;
    }

    @NonNull
    public UserRepository getUserRepository() {
        return userRepository;
    }

    @NonNull
    public FavoriteRepository getFavoriteRepository() {
        return favoriteRepository;
    }

    @NonNull
    public SearchRepository getSearchRepository() {
        return searchRepository;
    }

    @NonNull
    public ChatRepository getChatRepository() {
        return chatRepository;
    }

    @NonNull
    public SettingRepository getSettingRepository() {
        return settingRepository;
    }

}
