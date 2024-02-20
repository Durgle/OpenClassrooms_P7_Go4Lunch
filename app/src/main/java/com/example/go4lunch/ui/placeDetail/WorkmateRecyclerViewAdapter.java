package com.example.go4lunch.ui.placeDetail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.go4lunch.R;
import com.example.go4lunch.ui.placeDetail.viewState.WorkmateState;

public class WorkmateRecyclerViewAdapter extends ListAdapter<WorkmateState, WorkmateViewHolder> {

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

    public static final DiffUtil.ItemCallback<WorkmateState> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<WorkmateState>() {
                @Override
                public boolean areItemsTheSame(@NonNull WorkmateState oldWorkmate, @NonNull WorkmateState newWorkmate) {
                    return oldWorkmate.getId().equals(newWorkmate.getId());
                }
                @Override
                public boolean areContentsTheSame(@NonNull WorkmateState oldWorkmate, @NonNull WorkmateState newWorkmate) {
                    return oldWorkmate.equals(newWorkmate);
                }
            };


}
