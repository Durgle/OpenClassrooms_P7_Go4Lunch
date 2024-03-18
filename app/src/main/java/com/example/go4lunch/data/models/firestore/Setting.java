package com.example.go4lunch.data.models.firestore;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Setting {

    private String userId;
    private boolean enableNotifications;

    public Setting(String userId, boolean enableNotifications) {
        this.userId = userId;
        this.enableNotifications = enableNotifications;
    }

    public Setting() {}

    // --- GETTERS ---
    public String getUserId() {
        return userId;
    }
    public boolean getEnableNotifications() {
        return enableNotifications;
    }

    // --- SETTERS ---
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setEnableNotifications(boolean enableNotifications) {
        this.enableNotifications = enableNotifications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Setting setting = (Setting) o;
        return enableNotifications == setting.enableNotifications && Objects.equals(userId, setting.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, enableNotifications);
    }

    @NonNull
    @Override
    public String toString() {
        return "Setting{" +
                "userId='" + userId + '\'' +
                ", enableNotifications=" + enableNotifications +
                '}';
    }
}
