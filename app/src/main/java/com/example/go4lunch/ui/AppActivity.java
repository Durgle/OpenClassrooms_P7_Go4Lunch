package com.example.go4lunch.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityMainBinding;
import com.example.go4lunch.databinding.HeaderNavigationDrawerBinding;
import com.example.go4lunch.injection.ViewModelFactory;
import com.example.go4lunch.ui.map.MapsFragment;
import com.example.go4lunch.ui.placeList.PlaceListFragment;
import com.example.go4lunch.ui.workmate.WorkmatesFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AppActivity extends AppCompatActivity {

    private AuthViewModel viewModel;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(AuthViewModel.class);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_container, MapsFragment.newInstance())
                    .setReorderingAllowed(true)
                    .commit();
        }

        FirebaseUser currentUser = this.viewModel.getCurrentUser();
        if (currentUser == null) {
            redirectToLogin();
        }else{
            initLoggedUi(savedInstanceState);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        viewModel.getUserData();
        return super.onCreateView(name, context, attrs);
    }

    private void initLoggedUi(Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_container, MapsFragment.newInstance())
                    .setReorderingAllowed(true)
                    .commit();
        }

        ActivityMainBinding binding = ActivityMainBinding.inflate(LayoutInflater.from(this));

        binding.topAppBar.setNavigationOnClickListener(v -> binding.drawerMenu.open());
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
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
        });

        setContentView(binding.getRoot());

        NavigationView navigationView = findViewById(R.id.navigationView);

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.logout) {
                viewModel.logout(AppActivity.this).observe(this, result -> {
                    if(result) {
                        redirectToLogin();
                    }
                });
            }
            return true;
        });

        viewModel.getUserData().observe(this, user -> {
            if(user != null){
                HeaderNavigationDrawerBinding headerBinding = HeaderNavigationDrawerBinding.bind(navigationView.getHeaderView(0));
                headerBinding.drawerUsername.setText(user.getDisplayName());
                headerBinding.drawerUserEmail.setText(user.getEmail());
                Glide.with(getBaseContext())
                        .load(user.getUrlPicture() != null ? user.getUrlPicture() : R.drawable.blank_profile)
                        .into(headerBinding.drawerAvatar);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener);
    }

    private void redirectToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

}
