package com.example.go4lunch.data.models.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class PlaceOpeningHoursPeriodDetail {

    @SerializedName("day")
    @Expose
    private final int day;

    @SerializedName("time")
    @Expose
    private final String time;

    @SerializedName("date")
    @Expose
    private final String date;

    @SerializedName("truncated")
    @Expose
    private final Boolean truncated;

    public PlaceOpeningHoursPeriodDetail(int day, @NonNull String time, @Nullable String date, @Nullable Boolean truncated) {
        this.day = day;
        this.time = time;
        this.date = date;
        this.truncated = truncated;
    }

    public int getDay() {
        return day;
    }

    @NonNull
    public String getTime() {
        return time;
    }

    @Nullable
    public String getDate() {
        return date;
    }

    @Nullable
    public Boolean getTruncated() {
        return truncated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceOpeningHoursPeriodDetail that = (PlaceOpeningHoursPeriodDetail) o;
        return day == that.day && Objects.equals(time, that.time) && Objects.equals(date, that.date) && Objects.equals(truncated, that.truncated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, time, date, truncated);
    }

    @NonNull
    @Override
    public String toString() {
        return "PlaceOpeningHoursPeriodDetail{" +
                "day=" + day +
                ", time='" + time + '\'' +
                ", date='" + date + '\'' +
                ", truncated=" + truncated +
                '}';
    }
}
