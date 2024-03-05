package com.example.go4lunch.data.models.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class PlaceAutocompleteResponse {

    @NonNull
    @SerializedName("predictions")
    @Expose
    private final List<PlaceAutocompletePrediction> predictions;

    @NonNull
    @SerializedName("status")
    @Expose
    private final String status;

    @SerializedName("error_message")
    @Expose
    private final String errorMessage;

    @SerializedName("info_messages")
    @Expose
    private final List<String> infoMessages;

    public PlaceAutocompleteResponse(@NonNull List<PlaceAutocompletePrediction> predictions, @NonNull String status, String errorMessage, List<String> infoMessages) {
        this.predictions = predictions;
        this.status = status;
        this.errorMessage = errorMessage;
        this.infoMessages = infoMessages;
    }

    @NonNull
    public List<PlaceAutocompletePrediction> getPredictions() {
        return predictions;
    }

    @NonNull
    public String getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<String> getInfoMessages() {
        return infoMessages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceAutocompleteResponse that = (PlaceAutocompleteResponse) o;
        return Objects.equals(predictions, that.predictions) && Objects.equals(status, that.status) && Objects.equals(errorMessage, that.errorMessage) && Objects.equals(infoMessages, that.infoMessages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(predictions, status, errorMessage, infoMessages);
    }

    @NonNull
    @Override
    public String toString() {
        return "PlaceAutocompleteResponse{" +
                "predictions=" + predictions +
                ", status='" + status + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", infoMessages=" + infoMessages +
                '}';
    }
}
