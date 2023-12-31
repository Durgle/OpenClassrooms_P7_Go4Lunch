package com.example.go4lunch.data.models.map;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Bounds {

    @NonNull
    @SerializedName("northeast")
    @Expose
    private final LatLngLiteral northeast;

    @NonNull
    @SerializedName("southwest")
    @Expose
    private final LatLngLiteral southwest;

    public Bounds(@NonNull LatLngLiteral northeast, @NonNull LatLngLiteral southwest) {
        this.northeast = northeast;
        this.southwest = southwest;
    }

    @NonNull
    public LatLngLiteral getNortheast() {
        return northeast;
    }

    @NonNull
    public LatLngLiteral getSouthwest() {
        return southwest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bounds bounds = (Bounds) o;
        return Objects.equals(northeast, bounds.northeast) && Objects.equals(southwest, bounds.southwest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(northeast, southwest);
    }

    @NonNull
    @Override
    public String toString() {
        return "Bounds{" +
                "northeast=" + northeast +
                ", southwest=" + southwest +
                '}';
    }
}
