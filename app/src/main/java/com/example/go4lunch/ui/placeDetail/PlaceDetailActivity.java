package com.example.go4lunch.ui.placeDetail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityPlaceDetailBinding;
import com.example.go4lunch.injection.ViewModelFactory;
import com.example.go4lunch.ui.placeDetail.viewState.PlaceState;
import com.example.go4lunch.ui.placeDetail.viewState.UserState;
import com.example.go4lunch.ui.placeDetail.viewState.WorkmateState;
import com.example.go4lunch.utils.ViewUtils;
import com.example.go4lunch.utils.WorkerUtils;

import java.util.List;

public class PlaceDetailActivity extends AppCompatActivity {

    public static final String PLACE_ID = "place_id";
    private ActivityPlaceDetailBinding binding;
    private WorkmateRecyclerViewAdapter adapter;
    private PlaceDetailViewModel viewModel;

    /**
     * Start new place detail activity
     *
     * @param context Context
     * @param placeId Place Id
     */
    public static void startActivity(Context context, @NonNull String placeId) {
        Intent intent = new Intent(context, PlaceDetailActivity.class);
        intent.putExtra(PLACE_ID, placeId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityPlaceDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.adapter = new WorkmateRecyclerViewAdapter();
        binding.detailPlaceContent.workmateJoiningList.setAdapter(adapter);

        this.viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(PlaceDetailViewModel.class);
        this.viewModel.getPlaceViewState().observe(this, viewState -> {
            if (viewState != null) {
                fillWorkmateData(viewState.getWorkmates());
                fillPlaceData(viewState.getPlace());
                fillUserData(viewState.getUser(), viewState.getPlace());
            }
        });
    }

    /**
     * Fill User data
     *
     * @param userState User data
     */
    private void fillUserData(UserState userState, PlaceState placeState) {

        binding.detailPlaceContent.placeDetailLikeButton.setOnClickListener(view -> this.viewModel.toggleLike());
        binding.chooseRestaurantFab.setOnClickListener(
                view -> {
                    WorkerUtils.scheduleLunchNotification(getApplicationContext(), 12, 0, 0);
                    this.viewModel.chooseRestaurant();
                }
        );

        if (userState.isChoose()) {
            binding.chooseRestaurantFab.setColorFilter(getResources().getColor(R.color.green));
        } else {
            binding.chooseRestaurantFab.setColorFilter(getResources().getColor(R.color.lightGrey));
        }

        if (userState.isLike()) {
            ViewUtils.setTextViewDrawableColor(binding.detailPlaceContent.placeDetailLikeButton, R.color.yellow);
        } else {
            ViewUtils.setTextViewDrawableColor(binding.detailPlaceContent.placeDetailLikeButton, R.color.red);
        }
    }

    /**
     * Fill all place data
     *
     * @param placeState Place data
     */
    private void fillPlaceData(PlaceState placeState) {

        // Set place data
        binding.detailPlaceContent.placeDetailName.setText(placeState.getName());
        binding.detailPlaceContent.placeDetailName.append(placeState.getRating());
        binding.detailPlaceContent.placeDetailAddress.setText(placeState.getAddress());

        // Manage the phone button
        if (placeState.getPhone() != null) {
            binding.detailPlaceContent.placeDetailCallButton.setOnClickListener(view -> {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                startExternalActivity(intent, "tel:" + placeState.getPhone());
            });
            ViewUtils.setTextViewColor(binding.detailPlaceContent.placeDetailCallButton, R.color.red);
        } else {
            ViewUtils.setTextViewColor(binding.detailPlaceContent.placeDetailCallButton, R.color.lightGrey);
        }

        // Manage the website button
        if (placeState.getWebsite() != null) {
            binding.detailPlaceContent.placeDetailWebsiteButton.setOnClickListener(view -> {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                startExternalActivity(intent, placeState.getWebsite());
            });
            ViewUtils.setTextViewDrawableColor(binding.detailPlaceContent.placeDetailWebsiteButton, R.color.red);
        } else {

            ViewUtils.setTextViewDrawableColor(binding.detailPlaceContent.placeDetailWebsiteButton, R.color.lightGrey);
        }

        // Set place picture
        Glide.with(this)
                .load(placeState.getPhoto() != null ? placeState.getPhoto() : R.drawable.no_photos)
                .into(binding.detailPlacePhoto);
    }

    public void startExternalActivity(@NonNull Intent intent, String uri) {
        try {
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        } catch (Exception exception) {
            Log.e("PlaceDetailActivity", exception.toString());
            Toast.makeText(this, R.string.msg_error_general, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Fill the workmate list
     *
     * @param workmateStateList workmate list
     */
    private void fillWorkmateData(List<WorkmateState> workmateStateList) {
        adapter.submitList(workmateStateList);
    }

}