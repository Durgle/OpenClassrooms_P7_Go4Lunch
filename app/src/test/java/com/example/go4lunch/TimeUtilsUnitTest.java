package com.example.go4lunch;

import static org.junit.Assert.assertEquals;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.go4lunch.data.models.map.PlaceOpeningHours;
import com.example.go4lunch.utils.GoogleMapApiUtils;
import com.example.go4lunch.utils.TimeUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.LocalDateTime;
import java.util.Locale;

@RunWith(JUnit4.class)
public class TimeUtilsUnitTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {}

    @Test
    public void calculateMillisecondsUntilTarget_success() {
        LocalDateTime currentTime = LocalDateTime.of(2024,3,19,5,0,0);
        long expected = 7;
        long result = TimeUtils.calculateMillisecondsUntilTarget(currentTime,12,0,0);

        assertEquals(expected, result);
    }

    @Test
    public void formatDateTimeForDisplay_success() {
        long timestamp = 1710773006656L;
        String expected = "18 mars 2024, 15:43:26";
        String result = TimeUtils.formatDateTimeForDisplay(timestamp, Locale.FRENCH);

        assertEquals(expected, result);
    }

}
