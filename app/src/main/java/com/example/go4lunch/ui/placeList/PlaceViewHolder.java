package com.example.go4lunch.ui.placeList;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.PlaceListItemBinding;

public class PlaceViewHolder extends RecyclerView.ViewHolder {

    private final PlaceListItemBinding binding;

    public PlaceViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = PlaceListItemBinding.bind(itemView);
    }

    public void bind(@NonNull PlaceViewState place, @NonNull PlaceRecyclerViewAdapter.OnItemClickListener listener) {

        binding.placeName.setText(place.getName());
        binding.placeAddress.setText(place.getAddress());
        binding.placeDistance.setText(place.getDistance());
        binding.placeStatus.setText(place.getStatus());
        binding.placeRating.setText(place.getRating());
        Glide.with(itemView.getContext())
                .load(place.getPhoto() != null ? place.getPhoto() : R.drawable.no_photos)
                .into(binding.placePhoto);

        binding.placeItem.setOnClickListener(view -> listener.onClick(place.getId()));
    }

}
