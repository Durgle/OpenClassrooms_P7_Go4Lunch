package com.example.go4lunch.ui.setting;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.models.firestore.Setting;
import com.example.go4lunch.data.services.SettingRepository;
import com.example.go4lunch.utils.SingleLiveEvent;

public class SettingViewModel extends ViewModel {

    private final SettingRepository settingRepository;
    private final LiveData<SettingViewState> currentSettings;
    private final MutableLiveData<Boolean> enableNotification = new MutableLiveData<>();
    private final SingleLiveEvent<Boolean> saved = new SingleLiveEvent<>();

    public SettingViewModel(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
        LiveData<Setting> settingLiveData = this.settingRepository.getSettings();

        currentSettings = Transformations.map(settingLiveData, settings -> {

            if (settings != null) {
                return new SettingViewState(settings.getEnableNotifications());
            } else {
                return null;
            }
        });
    }

    public void onNotificationEnabledChange(boolean value){

        this.enableNotification.setValue(value);
    }

    public LiveData<SettingViewState> getSettings() {
        return this.currentSettings;
    }

    public LiveData<Boolean> isSaved() {
        return this.saved;
    }

    public void save() {
        Boolean currentChange = enableNotification.getValue();
        if(currentChange != null){
            this.settingRepository.saveSetting(currentChange);
            saved.setValue(true);
        } else {
            saved.setValue(false);
        }
    }

}
