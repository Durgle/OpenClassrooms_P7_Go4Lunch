package com.example.go4lunch.ui.workmate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.go4lunch.R;
import com.example.go4lunch.ui.placeList.PlaceRecyclerViewAdapter;

import java.util.Objects;

public class WorkmateRecyclerViewAdapter extends ListAdapter<WorkmateViewState,WorkmateViewHolder> {

    public interface OnItemClickListener {
        void onClick(String placeId);
    }

    private final OnItemClickListener listener;

    protected WorkmateRecyclerViewAdapter(OnItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public WorkmateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.workmate_list_item, parent, false);
        return new WorkmateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmateViewHolder holder, int position) {
        holder.bind(getItem(position),this.listener);
    }

    public static final DiffUtil.ItemCallback<WorkmateViewState> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<WorkmateViewState>() {
                @Override
                public boolean areItemsTheSame(@NonNull WorkmateViewState oldWorkmate, @NonNull WorkmateViewState newWorkmate) {
                    return Objects.equals(oldWorkmate.getId(), newWorkmate.getId());
                }
                @Override
                public boolean areContentsTheSame(@NonNull WorkmateViewState oldWorkmate, @NonNull WorkmateViewState newWorkmate) {
                    return oldWorkmate.equals(newWorkmate);
                }

            };


}
