package com.example.go4lunch.ui;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.models.firestore.User;
import com.example.go4lunch.data.services.UserRepository;
import com.example.go4lunch.utils.SingleLiveEvent;
import com.google.firebase.auth.FirebaseUser;

import javax.annotation.Nullable;

public class AuthViewModel extends ViewModel {

    private final UserRepository userRepository;
    protected final LiveData<User> userLiveData;

    public AuthViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
        userLiveData = this.userRepository.getUserData();
    }

    @Nullable
    public FirebaseUser getCurrentUser(){
        return this.userRepository.getCurrentUser();
    }

    public void createOrUpdateUser(){
        this.userRepository.createOrUpdateUser();
    }

    public LiveData<User> getUserData(){
        return userLiveData;
    }

    public SingleLiveEvent<Boolean> logout(Context context){
        return this.userRepository.signOut(context);
    }

}
