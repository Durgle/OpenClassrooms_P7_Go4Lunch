package com.example.go4lunch.data.services;

import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.Priority;

/**
 * Manage current location
 */
public class LocationRepository {

    private static final int LOCATION_REQUEST_INTERVAL_MS = 10_000;
    private static final float SMALLEST_DISPLACEMENT_THRESHOLD_METER = 25;

    @NonNull
    private final FusedLocationProviderClient fusedLocationProviderClient;
    @NonNull
    private final Looper looper;
    @NonNull
    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>(null);
    private LocationCallback callback;

    public LocationRepository(@NonNull FusedLocationProviderClient fusedLocationProviderClient, Looper looper) {
        this.fusedLocationProviderClient = fusedLocationProviderClient;
        this.looper = looper;
    }

    /**
     * Get the current location
     *
     * @return Current location live data
     */
    public LiveData<Location> getLocationLiveData() {
        return locationMutableLiveData;
    }

    /**
     * Enable location update
     */
    @RequiresPermission(anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
    public void startLocationRequest() {
        if (callback == null) {
            callback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    Location location = locationResult.getLastLocation();

                    locationMutableLiveData.setValue(location);
                }
            };
        }

        fusedLocationProviderClient.removeLocationUpdates(callback);

        fusedLocationProviderClient.requestLocationUpdates(
                new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY)
                        .setMinUpdateDistanceMeters(SMALLEST_DISPLACEMENT_THRESHOLD_METER)
                        .setIntervalMillis(LOCATION_REQUEST_INTERVAL_MS)
                        .build(),
                callback,
                looper
        );
    }

    /**
     * Remove location update
     */
    public void stopLocationRequest() {
        if (callback != null) {
            fusedLocationProviderClient.removeLocationUpdates(callback);
        }
    }

    /**
     * Create location with given latitude and longitude
     *
     * @param latitude Latitude
     * @param longitude Longitude
     * @return Location
     */
    public Location createLocation(double latitude, double longitude) {
        Location location = new Location("");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }

}
