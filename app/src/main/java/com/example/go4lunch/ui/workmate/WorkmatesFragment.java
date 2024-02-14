package com.example.go4lunch.ui.workmate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.databinding.FragmentWorkmatesBinding;
import com.example.go4lunch.injection.ViewModelFactory;
import com.example.go4lunch.ui.placeDetail.PlaceDetailActivity;
import com.example.go4lunch.ui.placeDetail.PlaceDetailViewModel;
import com.example.go4lunch.ui.placeList.PlaceListViewModel;

public class WorkmatesFragment extends Fragment {

    private FragmentWorkmatesBinding binding;
    private WorkmateRecyclerViewAdapter adapter;
    private WorkmateViewModel viewModel;

    public static WorkmatesFragment newInstance() {
        return new WorkmatesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.adapter = new WorkmateRecyclerViewAdapter(placeId -> PlaceDetailActivity.startActivity(requireContext(),placeId));
        this.viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(WorkmateViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.binding = FragmentWorkmatesBinding.inflate(inflater, container, false);
        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getUserList().observe(getViewLifecycleOwner(), list -> adapter.submitList(list));
        binding.workmateList.setAdapter(adapter);
    }

}