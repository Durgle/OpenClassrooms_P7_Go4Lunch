package com.example.go4lunch.ui.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.go4lunch.R;
import com.example.go4lunch.data.models.map.Place;
import com.example.go4lunch.databinding.FragmentMapsBinding;
import com.example.go4lunch.injection.ViewModelFactory;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.net.URL;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FragmentMapsBinding binding;
    private MapViewModel mViewModel;

    public static MapsFragment newInstance() {
        return new MapsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(MapViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMapsBinding.inflate(inflater, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        return binding.getRoot();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        requestPermission();
        try {
            boolean success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style));

            if (!success) {
                Log.e("MapsFragment", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsFragment", "Can't find style. Error: ", e);
        }

        mViewModel.getCurrentLocation().observe(getViewLifecycleOwner(),new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                if(location != null){
                    LatLng position = new LatLng(location.getLatitude(),location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15.0f));
                    mViewModel.getCurrentLocation().removeObserver(this);
                }
            }
        });

        mViewModel.getNearbyPlaces().observe(getViewLifecycleOwner(),placeList -> {
            for (Place place : placeList) {
                MarkerOptions marker = createMarker(place);
                if(marker != null) {
                    mMap.addMarker(marker);
                }
            }
        });

        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    public void requestPermission(){
        ActivityCompat.requestPermissions(
                requireActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                0
        );
    }

    public MarkerOptions createMarker(Place place) {
        if(place.getGeometry() != null) {
            LatLng position = new LatLng(
                    place.getGeometry().getLocation().getLat(),
                    place.getGeometry().getLocation().getLng()
            );

            return new MarkerOptions().position(position).title(place.getName());

        } else {
            return null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mViewModel.refresh();
    }
}