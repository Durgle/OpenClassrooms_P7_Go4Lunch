package com.example.go4lunch.ui;

import androidx.lifecycle.LiveData;

import com.example.go4lunch.data.models.firestore.User;
import com.example.go4lunch.data.services.SearchRepository;
import com.example.go4lunch.data.services.UserRepository;

public class AppViewModel extends AuthViewModel {

    private final SearchRepository searchRepository;

    public AppViewModel(UserRepository userRepository, SearchRepository searchRepository) {
        super(userRepository);
        this.searchRepository = searchRepository;
    }

    public void search(String input){
        if(input == null || input.equals("")) {
            this.searchRepository.searchPlace(null);
        } else {
            this.searchRepository.searchPlace(input);
        }

    }

    public String getChosenPlaceId() {
        User userData = userLiveData.getValue();
        if(userData != null && userData.getPlace() != null) {
            return userData.getPlace().getUid();
        } else {
            return null;
        }
    }

    public LiveData<String> getCurrentSearch() {
        return searchRepository.getSearch();
    }
}
