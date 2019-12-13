package com.cycling_advocacy.bumpy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.cycling_advocacy.bumpy.utils.PreferenceUtil;

public class OnboardingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        Button buttonOne = findViewById(R.id.button_next);
        buttonOne.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(OnboardingActivity.this, MainActivity.class);
                startActivity(intent);

                // since onboarding is shown only once, it is used to generate User Identifier
                PreferenceUtil.setDeviceUUID(OnboardingActivity.this);
                PreferenceUtil.setOnboardingScreenIsShown(OnboardingActivity.this);

                finish();
            }
        });
    }
}
