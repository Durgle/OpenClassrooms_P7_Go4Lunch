package com.example.go4lunch.data.models.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class PlaceOpeningHours {

    @SerializedName("open_now")
    @Expose
    private final Boolean openNow;

    @SerializedName("periods")
    @Expose
    private final List<PlaceOpeningHoursPeriod> periods;

    @SerializedName("special_days")
    @Expose
    private final List<PlaceSpecialDay> specialDays;

    @SerializedName("type")
    @Expose
    private final String type;

    @SerializedName("weekday_text")
    @Expose
    private final List<String> weekdayText;

    public PlaceOpeningHours(
            @Nullable Boolean openNow,
            @Nullable List<PlaceOpeningHoursPeriod> periods,
            @Nullable List<PlaceSpecialDay> specialDays,
            @Nullable String type,
            @Nullable List<String> weekdayText
    ) {
        this.openNow = openNow;
        this.periods = periods;
        this.specialDays = specialDays;
        this.type = type;
        this.weekdayText = weekdayText;
    }

    @Nullable
    public Boolean isOpenNow() {
        return openNow;
    }

    @Nullable
    public List<PlaceOpeningHoursPeriod> getPeriods() {
        return periods;
    }

    @Nullable
    public List<PlaceSpecialDay> getSpecialDays() {
        return specialDays;
    }

    @Nullable
    public String getType() {
        return type;
    }

    @Nullable
    public List<String> getWeekdayText() {
        return weekdayText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceOpeningHours that = (PlaceOpeningHours) o;
        return Objects.equals(openNow, that.openNow) && Objects.equals(periods, that.periods) && Objects.equals(specialDays, that.specialDays) && Objects.equals(type, that.type) && Objects.equals(weekdayText, that.weekdayText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(openNow, periods, specialDays, type, weekdayText);
    }

    @NonNull
    @Override
    public String toString() {
        return "PlaceOpeningHours{" +
                "openNow=" + openNow +
                ", periods=" + periods +
                ", specialDays=" + specialDays +
                ", type='" + type + '\'' +
                ", weekdayText=" + weekdayText +
                '}';
    }
}
