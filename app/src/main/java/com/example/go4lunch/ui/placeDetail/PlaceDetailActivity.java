package com.example.go4lunch.ui.placeDetail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.example.go4lunch.R;
import com.example.go4lunch.data.models.map.Place;
import com.example.go4lunch.databinding.ActivityPlaceDetailBinding;
import com.example.go4lunch.databinding.FragmentPlaceListBinding;
import com.example.go4lunch.injection.ViewModelFactory;

public class PlaceDetailActivity extends AppCompatActivity {

    public static final String PLACE_ID = "place_id";
    private ActivityPlaceDetailBinding binding;
    private PlaceDetailViewModel viewModel;

    public static void startActivity(Context context, @NonNull String placeId) {
        Intent intent = new Intent(context,PlaceDetailActivity.class);
        intent.putExtra(PLACE_ID, placeId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityPlaceDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(PlaceDetailViewModel.class);
        this.viewModel.getPlace().observe(this, place -> {
            if(place != null){
                binding.detailPlaceContent.placeDetailName.setText(place.getName());
                binding.detailPlaceContent.placeDetailAddress.setText(place.getAddress());
                binding.detailPlaceContent.placeDetailRating.setText(place.getRating());

                Glide.with(this)
                        .load(place.getPhoto() != null ? place.getPhoto() : R.drawable.no_photos)
                        .into(binding.detailPlacePhoto);
            }
        });
    }

}