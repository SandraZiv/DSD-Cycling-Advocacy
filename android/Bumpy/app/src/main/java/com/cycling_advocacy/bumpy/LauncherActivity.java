package com.cycling_advocacy.bumpy;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cycling_advocacy.bumpy.ui.MainActivity;
import com.cycling_advocacy.bumpy.ui.OnboardingActivity;
import com.cycling_advocacy.bumpy.utils.PreferenceUtil;

public class LauncherActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent;
        if (PreferenceUtil.shouldShowOnboardingScreen(this)) {
            intent = new Intent(this, OnboardingActivity.class);
        } else {
            intent = new Intent(this, MainActivity.class);
        }

        startActivity(intent);
        finish();
    }
}
