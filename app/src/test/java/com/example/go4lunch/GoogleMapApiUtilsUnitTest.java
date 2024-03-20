package com.example.go4lunch;

import static org.junit.Assert.assertEquals;

import android.location.Location;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.go4lunch.data.models.map.PlaceOpeningHours;
import com.example.go4lunch.utils.GoogleMapApiUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

@RunWith(JUnit4.class)
public class GoogleMapApiUtilsUnitTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {}

    @Test
    public void createPhotoUrl_success() {
        String photoReference = "165sd6fd1f26df6dfg63d1f6d3f1gfg";
        int maxWidth = 500;
        int maxHeight = 500;

        String expectedUrl = "https://maps.googleapis.com/maps/api/place/photo" +
                "?maxwidth=" + maxWidth +
                "&maxheight=" + maxHeight +
                "&photo_reference=" + photoReference +
                "&key=" + BuildConfig.MAPS_API_KEY;

        String result = GoogleMapApiUtils.createPhotoUrl(photoReference,maxWidth,maxHeight);

        assertEquals(expectedUrl, result);
    }

    @Test
    public void createStatus_success() {
        PlaceOpeningHours open = new PlaceOpeningHours(true,null,null,null,null);
        PlaceOpeningHours close = new PlaceOpeningHours(false,null,null,null,null);

        int result1 = GoogleMapApiUtils.createStatus(open);
        int result2 = GoogleMapApiUtils.createStatus(close);

        assertEquals(result1, R.string.place_status_open);
        assertEquals(result2, R.string.place_status_close);
    }

    @Test
    public void formatStarRating_success() {
        Float fiveStar = 5F;
        Float noDefinedStar = null;
        Float floatStar = 3.5F;

        String expectedResult1 = "★★★";
        String expectedResult2 = "";
        String expectedResult3 = "★★";


        String result1 = GoogleMapApiUtils.formatStarRating(fiveStar);
        String result2 = GoogleMapApiUtils.formatStarRating(noDefinedStar);
        String result3 = GoogleMapApiUtils.formatStarRating(floatStar);

        assertEquals(expectedResult1, result1);
        assertEquals(expectedResult2, result2);
        assertEquals(expectedResult3, result3);
    }

}
