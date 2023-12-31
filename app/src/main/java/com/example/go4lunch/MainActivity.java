package com.example.go4lunch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.example.go4lunch.databinding.ActivityMainBinding;
import com.example.go4lunch.ui.map.MapsFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_container, MapsFragment.newInstance())
                    .setReorderingAllowed(true)
                    .commit();
        }

        ActivityMainBinding binding = ActivityMainBinding.inflate(LayoutInflater.from(this));

        binding.topAppBar.setNavigationOnClickListener(v -> binding.drawerMenu.open());

        setContentView(binding.getRoot());
    }
}