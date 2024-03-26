package com.example.go4lunch.data.models.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class MapPlace {

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

    @SerializedName("photos")
    @Expose
    private final List<PlacePhoto> photos;

    @SerializedName("icon")
    @Expose
    private final String icon;

    @SerializedName("name")
    @Expose
    private final String name;

    @SerializedName("opening_hours")
    @Expose
    private final PlaceOpeningHours openingHours;

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

    @SerializedName("vicinity")
    @Expose
    private final String vicinity;

    public MapPlace(
            @Nullable String businessStatus,
            @Nullable String formattedAddress,
            @Nullable String formattedPhoneNumber,
            @Nullable Geometry geometry,
            @Nullable List<PlacePhoto> photos,
            @Nullable String icon,
            @NonNull String name,
            @Nullable PlaceOpeningHours openingHours,
            @NonNull String id,
            @Nullable Float rating,
            @Nullable Boolean reservable,
            @Nullable String website,
            @Nullable String vicinity
    ) {
        this.businessStatus = businessStatus;
        this.formattedAddress = formattedAddress;
        this.formattedPhoneNumber = formattedPhoneNumber;
        this.geometry = geometry;
        this.icon = icon;
        this.photos = photos;
        this.name = name;
        this.openingHours = openingHours;
        this.id = id;
        this.rating = rating;
        this.reservable = reservable;
        this.website = website;
        this.vicinity = vicinity;
    }

    @Nullable
    public String getBusinessStatus() {
        return businessStatus;
    }

    @Nullable
    public String getFormattedAddress() {
        return formattedAddress;
    }

    @Nullable
    public String getFormattedPhoneNumber() {
        return formattedPhoneNumber;
    }

    @Nullable
    public Geometry getGeometry() {
        return geometry;
    }

    @Nullable
    public List<PlacePhoto> getPhotos() {
        return photos;
    }

    @Nullable
    public String getIcon() {
        return icon;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Nullable
    public PlaceOpeningHours getOpeningHours() {
        return openingHours;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @Nullable
    public Float getRating() {
        return rating;
    }

    @Nullable
    public Boolean getReservable() {
        return reservable;
    }

    @Nullable
    public String getWebsite() {
        return website;
    }

    @Nullable
    public String getVicinity() {
        return vicinity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapPlace place = (MapPlace) o;
        return Objects.equals(businessStatus, place.businessStatus) && Objects.equals(formattedAddress, place.formattedAddress) && Objects.equals(formattedPhoneNumber, place.formattedPhoneNumber) && Objects.equals(geometry, place.geometry) && Objects.equals(photos, place.photos) && Objects.equals(icon, place.icon) && Objects.equals(name, place.name) && Objects.equals(openingHours, place.openingHours) && Objects.equals(id, place.id) && Objects.equals(rating, place.rating) && Objects.equals(reservable, place.reservable) && Objects.equals(website, place.website) && Objects.equals(vicinity, place.vicinity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(businessStatus, formattedAddress, formattedPhoneNumber, geometry, photos, icon, name, openingHours, id, rating, reservable, website, vicinity);
    }

    @NonNull
    @Override
    public String toString() {
        return "Place{" +
                "businessStatus='" + businessStatus + '\'' +
                ", formattedAddress='" + formattedAddress + '\'' +
                ", formattedPhoneNumber='" + formattedPhoneNumber + '\'' +
                ", geometry=" + geometry +
                ", photos=" + photos +
                ", icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", openingHours=" + openingHours +
                ", id='" + id + '\'' +
                ", rating=" + rating +
                ", reservable=" + reservable +
                ", website='" + website + '\'' +
                ", vicinity='" + vicinity + '\'' +
                '}';
    }
}
