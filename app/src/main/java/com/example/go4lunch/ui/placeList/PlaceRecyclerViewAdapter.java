package com.example.go4lunch.ui.placeList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.go4lunch.R;
import com.example.go4lunch.data.models.map.Place;

public class PlaceRecyclerViewAdapter extends ListAdapter<Place,PlaceViewHolder> {

    public interface OnItemClickListener {

        void onClick(String placeId);
    }

    private final OnItemClickListener listener;

    protected PlaceRecyclerViewAdapter(OnItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_list_item, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        holder.bind(getItem(position),this.listener);
    }

    public static final DiffUtil.ItemCallback<Place> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Place>() {
                @Override
                public boolean areItemsTheSame(@NonNull Place oldPlace, @NonNull Place newPlace) {
                    return oldPlace.getId().equals(newPlace.getId());
                }
                @Override
                public boolean areContentsTheSame(@NonNull Place oldPlace, @NonNull Place newPlace) {
                    return oldPlace.equals(newPlace);
                }
            };


}
