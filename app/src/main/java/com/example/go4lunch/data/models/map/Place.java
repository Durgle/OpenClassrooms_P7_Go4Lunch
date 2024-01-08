package com.example.go4lunch.data.models.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
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

    @SerializedName("photos")
    @Expose
    private final List<PlacePhoto> photos;

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
            @Nullable List<PlacePhoto> photos,
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
        this.photos = photos;
        this.name = name;
        this.id = id;
        this.rating = rating;
        this.reservable = reservable;
        this.website = website;
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
    public String getName() {
        return name;
    }

    @Nullable
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return Objects.equals(businessStatus, place.businessStatus) && Objects.equals(formattedAddress, place.formattedAddress) && Objects.equals(formattedPhoneNumber, place.formattedPhoneNumber) && Objects.equals(geometry, place.geometry) && Objects.equals(photos, place.photos) && Objects.equals(name, place.name) && Objects.equals(id, place.id) && Objects.equals(rating, place.rating) && Objects.equals(reservable, place.reservable) && Objects.equals(website, place.website);
    }

    @Override
    public int hashCode() {
        return Objects.hash(businessStatus, formattedAddress, formattedPhoneNumber, geometry, photos, name, id, rating, reservable, website);
    }

    @NonNull
    @Override
    public String toString() {
        return "Place{" +
                "businessStatus='" + businessStatus + '\'' +
                ", formattedAddress='" + formattedAddress + '\'' +
                ", formattedPhoneNumber='" + formattedPhoneNumber + '\'' +
                ", geometry=" + geometry +
                ", photos='" + photos + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", rating=" + rating +
                ", reservable=" + reservable +
                ", website='" + website + '\'' +
                '}';
    }
}
