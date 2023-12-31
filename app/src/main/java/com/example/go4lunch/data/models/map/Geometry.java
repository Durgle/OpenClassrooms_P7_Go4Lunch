package com.example.go4lunch.data.models.map;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Geometry {

    @NonNull
    @SerializedName("location")
    @Expose
    private final LatLngLiteral location;

    @NonNull
    @SerializedName("viewport")
    @Expose
    private final Bounds viewport;

    public Geometry(@NonNull LatLngLiteral location, @NonNull Bounds viewport) {
        this.location = location;
        this.viewport = viewport;
    }

    @NonNull
    public LatLngLiteral getLocation() {
        return location;
    }

    @NonNull
    public Bounds getViewport() {
        return viewport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Geometry geometry = (Geometry) o;
        return Objects.equals(location, geometry.location) && Objects.equals(viewport, geometry.viewport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, viewport);
    }

    @NonNull
    @Override
    public String toString() {
        return "Geometry{" +
                "location=" + location +
                ", viewport=" + viewport +
                '}';
    }
}
