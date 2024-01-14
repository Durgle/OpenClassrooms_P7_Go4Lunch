package com.example.go4lunch.ui.placeList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.databinding.FragmentPlaceListBinding;
import com.example.go4lunch.injection.ViewModelFactory;
import com.example.go4lunch.ui.placeDetail.PlaceDetailActivity;

public class PlaceListFragment extends Fragment {

    private FragmentPlaceListBinding binding;
    private PlaceListViewModel viewModel;
    private PlaceRecyclerViewAdapter adapter;

    public static PlaceListFragment newInstance() {
        return new PlaceListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.adapter = new PlaceRecyclerViewAdapter(placeId -> PlaceDetailActivity.startActivity(requireContext(),placeId));
        this.viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(PlaceListViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.binding = FragmentPlaceListBinding.inflate(inflater, container, false);
        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getNearbyPlaces().observe(getViewLifecycleOwner(), list -> adapter.submitList(list));
        binding.nearbyPlaceList.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        viewModel.refresh();
    }

}