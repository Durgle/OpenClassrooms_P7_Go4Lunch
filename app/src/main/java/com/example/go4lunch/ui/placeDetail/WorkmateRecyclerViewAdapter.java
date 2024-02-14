package com.example.go4lunch.ui.placeDetail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.go4lunch.R;

public class WorkmateRecyclerViewAdapter extends ListAdapter<WorkmateViewState, WorkmateViewHolder> {

    protected WorkmateRecyclerViewAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public WorkmateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.workmate_joining_item, parent, false);
        return new WorkmateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmateViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public static final DiffUtil.ItemCallback<WorkmateViewState> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<WorkmateViewState>() {
                @Override
                public boolean areItemsTheSame(@NonNull WorkmateViewState oldWorkmate, @NonNull WorkmateViewState newWorkmate) {
                    return oldWorkmate.getId().equals(newWorkmate.getId());
                }
                @Override
                public boolean areContentsTheSame(@NonNull WorkmateViewState oldWorkmate, @NonNull WorkmateViewState newWorkmate) {
                    return oldWorkmate.equals(newWorkmate);
                }
            };


}
