package com.example.go4lunch.data.models.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class PlaceOpeningHoursPeriod {

    @SerializedName("open")
    @Expose
    private final PlaceOpeningHoursPeriodDetail open;

    @SerializedName("close")
    @Expose
    private final PlaceOpeningHoursPeriodDetail close;

    public PlaceOpeningHoursPeriod(@NonNull PlaceOpeningHoursPeriodDetail open, @Nullable PlaceOpeningHoursPeriodDetail close) {
        this.open = open;
        this.close = close;
    }

    @NonNull
    public PlaceOpeningHoursPeriodDetail getOpen() {
        return open;
    }

    @Nullable
    public PlaceOpeningHoursPeriodDetail getClose() {
        return close;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceOpeningHoursPeriod that = (PlaceOpeningHoursPeriod) o;
        return Objects.equals(open, that.open) && Objects.equals(close, that.close);
    }

    @Override
    public int hashCode() {
        return Objects.hash(open, close);
    }

    @NonNull
    @Override
    public String toString() {
        return "PlaceOpeningHoursPeriod{" +
                "open=" + open +
                ", close=" + close +
                '}';
    }
}
