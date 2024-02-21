package com.example.go4lunch.ui;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.go4lunch.R;
import com.example.go4lunch.injection.ViewModelFactory;
import com.example.go4lunch.ui.AppActivity;
import com.example.go4lunch.ui.AuthViewModel;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private AuthViewModel viewModel;
    private FirebaseAuth auth;

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_ui);
        this.viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(AuthViewModel.class);
        auth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = this.viewModel.getCurrentUser();
        if (currentUser != null) {
            redirectToApp();
        }else {
            createSignInIntent();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    public void createSignInIntent() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout
                .Builder(R.layout.firebase_ui_cutom_layout)
                .setGoogleButtonId(R.id.googleSignInButton)
                .setEmailButtonId(R.id.emailSignInButton)
                .build();

        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setTheme(R.style.Base_Theme_Go4Lunch)
                .setAvailableProviders(providers)
                .setAuthMethodPickerLayout(customLayout)
                .build();
        signInLauncher.launch(signInIntent);
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            viewModel.createOrUpdateUser();
            redirectToApp();
        } else {
            Log.e("LoginActivity",result.toString());
        }
    }

    private void redirectToApp() {
        Intent intent = new Intent(this, AppActivity.class);
        startActivity(intent);
        finish();
    }

    private void reload() { }

    private void updateUI(FirebaseUser user) {

    }

}