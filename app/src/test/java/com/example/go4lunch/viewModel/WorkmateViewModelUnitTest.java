package com.example.go4lunch.viewModel;

import static org.junit.Assert.assertEquals;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.models.firestore.Place;
import com.example.go4lunch.data.models.firestore.User;
import com.example.go4lunch.data.services.UserRepository;
import com.example.go4lunch.ui.workmate.WorkmateViewModel;
import com.example.go4lunch.ui.workmate.WorkmateViewState;
import com.example.go4lunch.utils.LiveDataTestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RunWith(JUnit4.class)
public class WorkmateViewModelUnitTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private UserRepository userRepository;

    private static final User workmate1 = new User("1", "User 1", "user1@gmail.com", null);
    private static final User workmate2 = new User("2", "User 2", "user2@gmail.com", null);
    private static final User workmate3 = new User("3", "User 3", "user3@gmail.com", null);
    private static final WorkmateViewState workmateViewState1 = new WorkmateViewState("1", "User 1", null, null);
    private static final WorkmateViewState workmateViewState2 = new WorkmateViewState("2", "User 2", null, null);
    private static final WorkmateViewState workmateViewState3 = new WorkmateViewState("3", "User 3", null, null);

    @NonNull
    private List<User> getFakeWorkmates() {
        return new ArrayList<>(Arrays.asList(workmate1, workmate2, workmate3));
    }

    @NonNull
    private User getFakeUser() {
        User fakeUser = new User("1", "Fake User", "fakeuser1@gmail.com", null);
        fakeUser.setPlace(new Place("1", "Fake Place", "15 fake address"));
        return fakeUser;
    }

    @Before
    public void setup() {
        userRepository = Mockito.mock(UserRepository.class);
        LiveData<List<User>> workmatesLiveData = new MutableLiveData<>(getFakeWorkmates());
        Mockito.when(userRepository.getAllWorkmates())
                .thenReturn(workmatesLiveData);
    }

    @Test
    public void mapUser_success() {
        User fakeUser = getFakeUser();
        WorkmateViewState result = WorkmateViewModel.mapUser(getFakeUser());

        assertEquals(result.getId(), fakeUser.getUid());
        assertEquals(result.getWorkmateName(), fakeUser.getDisplayName());
        assertEquals(result.getPicture(), fakeUser.getUrlPicture());
        assertEquals(result.getPlace(), fakeUser.getPlace());

    }

    @Test
    public void getUserList_success() throws InterruptedException {
        WorkmateViewModel viewModel = new WorkmateViewModel(userRepository);
        List<WorkmateViewState> workmatesResult = LiveDataTestUtil.getOrAwaitValue(viewModel.getUserList());

        List<WorkmateViewState> expected = new ArrayList<>(
                Arrays.asList(workmateViewState1, workmateViewState2, workmateViewState3)
        );

        assertEquals(expected, workmatesResult);
    }

}
