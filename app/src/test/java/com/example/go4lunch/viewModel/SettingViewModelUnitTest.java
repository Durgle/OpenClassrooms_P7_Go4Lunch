package com.example.go4lunch.viewModel;

import static org.junit.Assert.assertEquals;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.models.firestore.Place;
import com.example.go4lunch.data.models.firestore.Setting;
import com.example.go4lunch.data.models.firestore.User;
import com.example.go4lunch.data.services.SearchRepository;
import com.example.go4lunch.data.services.SettingRepository;
import com.example.go4lunch.data.services.UserRepository;
import com.example.go4lunch.ui.setting.SettingViewModel;
import com.example.go4lunch.ui.setting.SettingViewState;
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
public class SettingViewModelUnitTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private SettingRepository settingRepository;

    private static final Setting fakeSetting = new Setting("1", true);

    @Before
    public void setup() {
        settingRepository = Mockito.mock(SettingRepository.class);
        LiveData<Setting> settingLiveData = new MutableLiveData<>(fakeSetting);
        Mockito.when(settingRepository.getSettings())
                .thenReturn(settingLiveData);
    }

    @Test
    public void getSettings_success() throws InterruptedException {
        SettingViewModel viewModel = new SettingViewModel(settingRepository);
        SettingViewState settingsResult = LiveDataTestUtil.getOrAwaitValue(viewModel.getSettings());
        SettingViewState expected = new SettingViewState(fakeSetting.getEnableNotifications());

        assertEquals(expected, settingsResult);
    }

    @Test
    public void save_success() throws InterruptedException {
        SettingViewModel viewModel = new SettingViewModel(settingRepository);
        viewModel.onNotificationEnabledChange(true);
        viewModel.save();
        Boolean result = LiveDataTestUtil.getOrAwaitValue(viewModel.isSaved());

        assertEquals(result, true);
    }

    @Test
    public void save_failed() throws InterruptedException {
        SettingViewModel viewModel = new SettingViewModel(settingRepository);
        viewModel.save();
        Boolean result = LiveDataTestUtil.getOrAwaitValue(viewModel.isSaved());

        assertEquals(result, false);
    }

}
