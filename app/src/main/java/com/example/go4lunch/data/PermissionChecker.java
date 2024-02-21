package com.example.go4lunch.data;

import android.app.Application;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

/**
 * Check permission
 */
public class PermissionChecker {

    @NonNull
    private final Application application;

    public PermissionChecker(@NonNull Application application) {
        this.application = application;
    }

    /**
     * Check if the location permission is granted
     *
     * @return Location permission status
     */
    public boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(application, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED;
    }
}
