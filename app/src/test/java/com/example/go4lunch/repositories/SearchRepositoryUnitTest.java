package com.example.go4lunch.repositories;

import static org.junit.Assert.assertEquals;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.example.go4lunch.data.models.firestore.Message;
import com.example.go4lunch.data.services.GoogleMapRepository;
import com.example.go4lunch.data.services.SearchRepository;
import com.example.go4lunch.utils.LiveDataTestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Date;

@RunWith(JUnit4.class)
public class SearchRepositoryUnitTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private SearchRepository searchRepository;

    @Before
    public void setup() {
        searchRepository = new SearchRepository();
    }

    @Test
    public void testLiveData_success() throws InterruptedException {
        String searchInput = "Restaurant";
        searchRepository.searchPlace(searchInput);
        String currentSearch = LiveDataTestUtil.getOrAwaitValue(searchRepository.getSearch());
        assertEquals(searchInput, currentSearch);
    }

}
