package com.example.go4lunch.ui.placeList;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.PlaceListItemBinding;
import com.example.go4lunch.utils.ViewUtils;

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
        Integer status = place.getStatus();
        if (status != null) {
            binding.placeStatus.setText(status);
        } else {
            binding.placeStatus.setText("");
        }
        long workmateCount = place.getWorkmateCount();
        if (workmateCount > 0) {
            binding.placeWorkmateAmount.setText(
                    itemView.getContext().getString(R.string.place_workmate_amount, workmateCount)
            );
//            ViewUtils.setLeftTextViewIcon(
//                    binding.placeWorkmateAmount,
//                    ContextCompat.getDrawable(itemView.getContext(), R.drawable.baseline_person_outline_24)
//            );
            binding.placeWorkmateAmount.setVisibility(View.VISIBLE);
        } else {
            binding.placeWorkmateAmount.setText("");
            binding.placeWorkmateAmount.setVisibility(View.INVISIBLE);
        }
        binding.placeRating.setText(place.getRating());
        Glide.with(itemView.getContext())
                .load(place.getPhoto() != null ? place.getPhoto() : R.drawable.no_photos)
                .into(binding.placePhoto);

        binding.placeItem.setOnClickListener(view -> listener.onClick(place.getId()));
    }

}
