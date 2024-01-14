package com.example.go4lunch.ui.placeList;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.data.models.map.Place;
import com.example.go4lunch.databinding.PlaceListItemBinding;

public class PlaceViewHolder extends RecyclerView.ViewHolder {

    private final PlaceListItemBinding binding;

    public PlaceViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = PlaceListItemBinding.bind(itemView);
    }

    public void bind(@NonNull Place place, @NonNull PlaceRecyclerViewAdapter.OnItemClickListener listener) {

        binding.placeName.setText(place.getName());
        binding.placeAddress.setText(place.getFormattedAddress());
        if(place.getOpeningHours() != null && place.getOpeningHours().isOpenNow() == Boolean.TRUE){
            binding.placeStatus.setText("Open");
        } else {
            binding.placeStatus.setText("Close");
        }
        if(place.getRating() != null) {
            binding.placeRating.setText(getStarRating(place.getRating()));
        }

        binding.placeItem.setOnClickListener(view -> listener.onClick(place.getId()));
    }

    public String getStarRating(double rating) {
        StringBuilder stars = new StringBuilder();
        int numberOfStars = (int) rating;

        for (int i = 0; i < numberOfStars; i++) {
            stars.append("\u2605");
        }

        return stars.toString();
    }
}
