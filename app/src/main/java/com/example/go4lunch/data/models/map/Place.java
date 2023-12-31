package com.example.go4lunch.data.models.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Place {

    @SerializedName("business_status")
    @Expose
    private final String businessStatus;

    @SerializedName("formatted_address")
    @Expose
    private final String formattedAddress;

    @SerializedName("formatted_phone_number")
    @Expose
    private final String formattedPhoneNumber;

    @SerializedName("geometry")
    @Expose
    private final Geometry geometry;

    @SerializedName("icon")
    @Expose
    private final String icon;

    @SerializedName("name")
    @Expose
    private final String name;

    @SerializedName("place_id")
    @Expose
    private final String id;

    @SerializedName("rating")
    @Expose
    private final Float rating;

    @SerializedName("reservable")
    @Expose
    private final Boolean reservable;

    @SerializedName("website")
    @Expose
    private final String website;

    public Place(
            @Nullable String businessStatus,
            @Nullable String formattedAddress,
            @Nullable String formattedPhoneNumber,
            @Nullable Geometry geometry,
            @Nullable String icon,
            @Nullable String name,
            @Nullable String id,
            @Nullable Float rating,
            @Nullable Boolean reservable,
            @Nullable String website
    ) {
        this.businessStatus = businessStatus;
        this.formattedAddress = formattedAddress;
        this.formattedPhoneNumber = formattedPhoneNumber;
        this.geometry = geometry;
        this.icon = icon;
        this.name = name;
        this.id = id;
        this.rating = rating;
        this.reservable = reservable;
        this.website = website;
    }

    public String getBusinessStatus() {
        return businessStatus;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public String getFormattedPhoneNumber() {
        return formattedPhoneNumber;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public Float getRating() {
        return rating;
    }

    public Boolean getReservable() {
        return reservable;
    }

    public String getWebsite() {
        return website;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return Objects.equals(businessStatus, place.businessStatus) && Objects.equals(formattedAddress, place.formattedAddress) && Objects.equals(formattedPhoneNumber, place.formattedPhoneNumber) && Objects.equals(geometry, place.geometry) && Objects.equals(icon, place.icon) && Objects.equals(name, place.name) && Objects.equals(id, place.id) && Objects.equals(rating, place.rating) && Objects.equals(reservable, place.reservable) && Objects.equals(website, place.website);
    }

    @Override
    public int hashCode() {
        return Objects.hash(businessStatus, formattedAddress, formattedPhoneNumber, geometry, icon, name, id, rating, reservable, website);
    }

    @NonNull
    @Override
    public String toString() {
        return "Place{" +
                "businessStatus='" + businessStatus + '\'' +
                ", formattedAddress='" + formattedAddress + '\'' +
                ", formattedPhoneNumber='" + formattedPhoneNumber + '\'' +
                ", geometry=" + geometry +
                ", icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", rating=" + rating +
                ", reservable=" + reservable +
                ", website='" + website + '\'' +
                '}';
    }
}
