package com.example.go4lunch.repositories;

import static org.mockito.Mockito.mock;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.go4lunch.data.services.SettingRepository;
import com.example.go4lunch.data.services.UserRepository;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class UserRepositoryUnitTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private AuthUI authUi;

    private UserRepository userRepository;

    @Before
    public void setup() {
        firebaseFirestore = mock(FirebaseFirestore.class);
        firebaseAuth = mock(FirebaseAuth.class);
        authUi = mock(AuthUI.class);
        userRepository = new UserRepository(firebaseFirestore, authUi, firebaseAuth);
    }

    @Test
    public void signOut_success() {

    }

    @Test
    public void createOrUpdateUser_success() {

    }

    @Test
    public void updateChosenRestaurant_success() {

    }

    @Test
    public void getUserData_success() {

    }

    @Test
    public void getAllWorkmates_success() {

    }

    @Test
    public void getAllUsers_success() {

    }

    @Test
    public void getWorkmatesForPlace_success() {

    }

    @Test
    public void getLunchNotificationData_success() {

    }

}
