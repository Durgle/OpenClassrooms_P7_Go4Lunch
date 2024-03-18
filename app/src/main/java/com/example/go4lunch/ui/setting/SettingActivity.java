package com.example.go4lunch.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivitySettingBinding;
import com.example.go4lunch.injection.ViewModelFactory;

public class SettingActivity extends AppCompatActivity {

    private ActivitySettingBinding binding;
    private SettingViewModel viewModel;

    /**
     * Start new place detail activity
     *
     * @param context Context
     */
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivitySettingBinding.inflate(getLayoutInflater());
        this.viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(SettingViewModel.class);

        setContentView(binding.getRoot());
        binding.settingToolbar.setNavigationOnClickListener(view -> finish());
        binding.settingToolbar.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.saveButton) {
                viewModel.save();
            }
            return true;
        });

        binding.enableNotification.setOnCheckedChangeListener(
                (buttonView, isChecked) -> viewModel.onNotificationEnabledChange(isChecked)
        );

        this.viewModel.getSettings().observe(this, settings -> {
            if (settings != null) {
                binding.enableNotification.setChecked(settings.isNotificationEnabled());
            }
        });

        this.viewModel.isSaved().observe(this, saved -> {
            if (saved) {
                Toast.makeText(getBaseContext(), R.string.msg_success_setting_save, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getBaseContext(), R.string.msg_error_nothing_save, Toast.LENGTH_SHORT).show();
            }
        });
    }

}