package com.cycling_advocacy.bumpy.ui.achievements;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.cycling_advocacy.bumpy.R;

public class AchievementsFragment extends Fragment {

    private AchievementsViewModel achievementsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        achievementsViewModel = ViewModelProviders.of(this).get(AchievementsViewModel.class);
        return inflater.inflate(R.layout.fragment_achievements, container, false);
    }
}