package com.example.go4lunch.repositories;

import static org.mockito.Mockito.mock;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.go4lunch.data.services.FavoriteRepository;
import com.example.go4lunch.data.services.SettingRepository;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SettingRepositoryUnitTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private SettingRepository settingRepository;

    @Before
    public void setup() {
        firebaseFirestore = mock(FirebaseFirestore.class);
        firebaseAuth = mock(FirebaseAuth.class);
        settingRepository = new SettingRepository(firebaseFirestore, firebaseAuth);
    }

    @Test
    public void saveSetting_success() {

    }

    @Test
    public void getSettings_success() {

    }

    @Test
    public void getSyncSettings_success() {

    }

}
