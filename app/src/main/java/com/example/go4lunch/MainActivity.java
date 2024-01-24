package com.example.go4lunch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;

import com.example.go4lunch.databinding.ActivityMainBinding;
import com.example.go4lunch.ui.LoginActivity;
import com.example.go4lunch.ui.map.MapsFragment;
import com.example.go4lunch.ui.placeList.PlaceListFragment;
import com.example.go4lunch.ui.workmate.WorkmatesFragment;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }else{
            createLoggedUi(savedInstanceState);
        }

    }

    private void createLoggedUi(Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_container, MapsFragment.newInstance())
                    .setReorderingAllowed(true)
                    .commit();
        }

        ActivityMainBinding binding = ActivityMainBinding.inflate(LayoutInflater.from(this));

        binding.topAppBar.setNavigationOnClickListener(v -> binding.drawerMenu.open());
        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                int itemId = item.getItemId();
                if (itemId == R.id.list_view) {
                    fragment = new PlaceListFragment();
                } else if (itemId == R.id.workmates) {
                    fragment = new WorkmatesFragment();
                } else {
                    fragment = new MapsFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
                return true;
            }
        });

        setContentView(binding.getRoot());
    }


}