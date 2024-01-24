package com.example.go4lunch.ui.placeDetail;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.go4lunch.data.models.map.PlaceOpeningHours;
import com.example.go4lunch.utils.GoogleMapApiUtils;

import java.util.Objects;

public class PlaceViewState {

    private final String id;

    private final String name;

    private final String address;

    private final String phone;

    private final String rating;

    private final boolean like;

    private final String website;

    private final String photoReference;

    public PlaceViewState(
            @NonNull String id,
            @NonNull String name,
            @Nullable String address,
            @Nullable PlaceOpeningHours placeOpeningHours,
            @Nullable String phone,
            @Nullable Float rating,
            boolean like,
            @Nullable String website,
            @Nullable String photoReference
    ) {
        this.id = id;
        this.name = name;
        this.address = (address != null)? address : "";
        this.phone = (phone != null)? phone : "";
        this.rating = GoogleMapApiUtils.formatStarRating(rating);
        this.like = like;
        this.website = (website != null)? website : "";
        this.photoReference = photoReference != null ? GoogleMapApiUtils.createPhotoUrl(photoReference,800,800) : null;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    @NonNull
    public String getRating() {
        return rating;
    }

    @Nullable
    public String getPhoto() {
        return photoReference;
    }

    @NonNull
    public String getPhone() { return phone; }

    public boolean isLike() { return like; }

    @NonNull
    public String getWebsite() { return website; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceViewState that = (PlaceViewState) o;
        return like == that.like && Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(address, that.address) && Objects.equals(phone, that.phone) && Objects.equals(rating, that.rating) && Objects.equals(website, that.website) && Objects.equals(photoReference, that.photoReference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, phone, rating, like, website, photoReference);
    }

    @NonNull
    @Override
    public String toString() {
        return "PlaceViewState{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", rating='" + rating + '\'' +
                ", like=" + like +
                ", website='" + website + '\'' +
                ", photoReference='" + photoReference + '\'' +
                '}';
    }
}
