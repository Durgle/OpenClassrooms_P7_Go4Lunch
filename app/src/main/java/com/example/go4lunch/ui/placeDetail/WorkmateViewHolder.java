package com.example.go4lunch.ui.placeDetail;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.WorkmateJoiningItemBinding;
import com.example.go4lunch.ui.placeDetail.viewState.WorkmateState;

public class WorkmateViewHolder extends RecyclerView.ViewHolder {

    private final WorkmateJoiningItemBinding binding;
    private final Context context;

    public WorkmateViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = WorkmateJoiningItemBinding.bind(itemView);
        this.context = binding.getRoot().getContext();
    }

    public void bind(@NonNull WorkmateState workmate) {
        Glide.with(context)
                .load(workmate.getPicture() != null ? workmate.getPicture() : R.drawable.blank_profile)
                .into(binding.workmateJoiningPicture);

        binding.workmateJoiningText.setText(
            context.getString(R.string.workmate_joining,workmate.getWorkmateName())
        );

    }
}
