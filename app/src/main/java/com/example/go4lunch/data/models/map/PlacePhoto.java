package com.example.go4lunch.data.models.map;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class PlacePhoto {

    @SerializedName("height")
    @Expose
    private final long height;

    @NonNull
    @SerializedName("html_attributions")
    @Expose
    private final List<String> htmlAttributions;

    @NonNull
    @SerializedName("photo_reference")
    @Expose
    private final String photoReference;

    @SerializedName("width")
    @Expose
    private final int width;

    public PlacePhoto(long height, @NonNull List<String> htmlAttributions, @NonNull String photoReference, int width) {
        this.height = height;
        this.htmlAttributions = htmlAttributions;
        this.photoReference = photoReference;
        this.width = width;
    }

    public long getHeight() {
        return height;
    }

    @NonNull
    public List<String> getHtmlAttributions() {
        return htmlAttributions;
    }

    @NonNull
    public String getPhotoReference() {
        return photoReference;
    }

    public int getWidth() {
        return width;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlacePhoto that = (PlacePhoto) o;
        return height == that.height && width == that.width && htmlAttributions.equals(that.htmlAttributions) && photoReference.equals(that.photoReference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(height, htmlAttributions, photoReference, width);
    }

    @NonNull
    @Override
    public String toString() {
        return "PlacePhoto{" +
                "height=" + height +
                ", htmlAttributions=" + htmlAttributions +
                ", photoReference='" + photoReference + '\'' +
                ", width=" + width +
                '}';
    }
}
