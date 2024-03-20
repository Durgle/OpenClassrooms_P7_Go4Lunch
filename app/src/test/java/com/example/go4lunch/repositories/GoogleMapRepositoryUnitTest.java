package com.example.go4lunch.repositories;

import static org.mockito.Mockito.mock;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.go4lunch.data.api.GoogleMapApi;
import com.example.go4lunch.data.services.GoogleMapRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class GoogleMapRepositoryUnitTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private GoogleMapApi googleMapApi;

    private GoogleMapRepository googleMapRepository;

    @Before
    public void setup() {
        googleMapApi = mock(GoogleMapApi.class);
        googleMapRepository = new GoogleMapRepository(googleMapApi);
    }

    @Test
    public void getNearbyPlaceLiveData_success() {

    }

    @Test
    public void getPlaceAutocomplete_success() {

    }

    @Test
    public void getPlaceDetailsLiveData_success() {

    }

}
