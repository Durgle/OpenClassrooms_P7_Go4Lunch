package com.example.go4lunch.data.models.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class PlaceSpecialDay {

    @SerializedName("date")
    @Expose
    private final String date;

    @SerializedName("exceptional_hours")
    @Expose
    private final Boolean exceptionalHours;

    public PlaceSpecialDay(@Nullable String date,@Nullable Boolean exceptionalHours) {
        this.date = date;
        this.exceptionalHours = exceptionalHours;
    }

    public String getDate() {
        return date;
    }

    public Boolean getExceptionalHours() {
        return exceptionalHours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceSpecialDay that = (PlaceSpecialDay) o;
        return Objects.equals(date, that.date) && Objects.equals(exceptionalHours, that.exceptionalHours);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, exceptionalHours);
    }

    @NonNull
    @Override
    public String toString() {
        return "PlaceSpecialDay{" +
                "date='" + date + '\'' +
                ", exceptionalHours=" + exceptionalHours +
                '}';
    }
}
