package com.example.go4lunch.ui;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.models.firestore.User;
import com.example.go4lunch.data.services.UserRepository;
import com.google.firebase.auth.FirebaseUser;

import javax.annotation.Nullable;

public class AuthViewModel extends ViewModel {

    private final UserRepository userRepository;

    public AuthViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Nullable
    public FirebaseUser getCurrentUser(){
        return this.userRepository.getCurrentUser();
    }

    public void createOrUpdateUser(){
        this.userRepository.createOrUpdateUser();
    }

    public LiveData<User> getUserData(){
        return this.userRepository.getUserData();
    }

    public LiveData<Boolean> isLogged(){
        return this.userRepository.isLogged();
    }

    public void logout(Context context){
        this.userRepository.signOut(context);
    }

}
