package com.example.go4lunch.ui.placeDetail.viewState;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.go4lunch.utils.GoogleMapApiUtils;

import java.util.Objects;

public class PlaceState {

    private final String id;

    private final String name;

    private final String address;

    private final String phone;

    private final String rating;

    private final String website;

    private final String photoReference;

    public PlaceState(
            @NonNull String id,
            @NonNull String name,
            @Nullable String address,
            @Nullable String phone,
            @Nullable Float rating,
            @Nullable String website,
            @Nullable String photoReference
    ) {
        this.id = id;
        this.name = name;
        this.address = (address != null)? address : "";
        this.phone = phone;
        this.rating = GoogleMapApiUtils.formatStarRating(rating);
        this.website = website;
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
    public SpannableString getRating() {
        String ratingString = " " + rating;
        SpannableString spannableRating = new SpannableString(ratingString);
        spannableRating.setSpan(
                new ForegroundColorSpan(Color.YELLOW), 0, ratingString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        return spannableRating;
    }

    @Nullable
    public String getPhoto() {
        return photoReference;
    }

    public String getPhone() { return phone; }

    public String getWebsite() { return website; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceState that = (PlaceState) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(address, that.address) && Objects.equals(phone, that.phone) && Objects.equals(rating, that.rating) && Objects.equals(website, that.website) && Objects.equals(photoReference, that.photoReference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, phone, rating, website, photoReference);
    }

    @NonNull
    @Override
    public String toString() {
        return "PlaceDetailState{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", rating='" + rating + '\'' +
                ", website='" + website + '\'' +
                ", photoReference='" + photoReference + '\'' +
                '}';
    }
}
