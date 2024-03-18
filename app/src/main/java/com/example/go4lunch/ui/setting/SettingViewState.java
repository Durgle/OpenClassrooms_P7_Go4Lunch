package com.example.go4lunch.ui.setting;

import androidx.annotation.NonNull;

import java.util.Objects;

public class SettingViewState {

    private final boolean notificationEnable;

    public SettingViewState(boolean notificationEnable) {
        this.notificationEnable = notificationEnable;
    }

    public boolean isNotificationEnabled() {
        return notificationEnable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SettingViewState that = (SettingViewState) o;
        return notificationEnable == that.notificationEnable;
    }

    @Override
    public int hashCode() {
        return Objects.hash(notificationEnable);
    }

    @NonNull
    @Override
    public String toString() {
        return "SettingViewState{" +
                "notificationEnable=" + notificationEnable +
                '}';
    }
}
