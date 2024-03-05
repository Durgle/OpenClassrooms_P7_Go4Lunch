package com.example.go4lunch.ui.workmate;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.models.firestore.User;
import com.example.go4lunch.data.services.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class WorkmateViewModel extends ViewModel {

    private final UserRepository userRepository;
    private LiveData<List<WorkmateViewState>> workmateList;

    public static WorkmateViewState mapUser(User user){
        return new WorkmateViewState(
                user.getUid(),
                user.getDisplayName(),
                user.getPlace(),
                user.getUrlPicture()
        );
    }

    public WorkmateViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;

        workmateList = Transformations.map(userRepository.getAllWorkmates(), userList -> {
            List<WorkmateViewState> list = new ArrayList<>();
            for (User user : userList) {
                list.add(mapUser(user));
            }
            return list;
        });
    }

    public LiveData<List<WorkmateViewState>> getUserList(){
        return workmateList;
    }

}
