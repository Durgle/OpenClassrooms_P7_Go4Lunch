package com.example.go4lunch.data.services;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * Manage search
 */
public class SearchRepository {

    private MutableLiveData<String> inputLiveData = new MutableLiveData<>();

    public void searchPlace(String input) {
        inputLiveData.setValue(input);
    }

    public LiveData<String> getSearch(){
        return inputLiveData;
    }

}
