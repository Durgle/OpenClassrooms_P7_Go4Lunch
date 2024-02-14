package com.example.go4lunch.ui.workmate;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.WorkmateListItemBinding;

public class WorkmateViewHolder extends RecyclerView.ViewHolder {

    private final WorkmateListItemBinding binding;
    private final Context context;

    public WorkmateViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = WorkmateListItemBinding.bind(itemView);
        this.context = binding.getRoot().getContext();
    }

    public void bind(@NonNull WorkmateViewState workmate, @NonNull WorkmateRecyclerViewAdapter.OnItemClickListener listener) {
        Glide.with(context)
                .load(workmate.getPicture() != null ? workmate.getPicture() : R.drawable.blank_profile)
                .into(binding.workmatePicture);

        if(workmate.getPlace() != null){
            binding.workmatePlace.setText(
                context.getString(R.string.workmate_place,workmate.getWorkmateName(),workmate.getPlace().getName())
            );
            binding.workmatePlaceInformation.setOnClickListener(view -> listener.onClick(workmate.getPlace().getUid()));
        } else {
            binding.workmatePlace.setText(
                context.getString(R.string.workmate_place_empty,workmate.getWorkmateName())
            );
            binding.workmatePlace.setTextColor(ContextCompat.getColor(context, R.color.lightGrey));
            binding.workmatePlace.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
        }

    }
}
