package com.cycling_advocacy.bumpy.achievements.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cycling_advocacy.bumpy.R;
import com.cycling_advocacy.bumpy.achievements.Achievement;
import com.cycling_advocacy.bumpy.achievements.util.AchievementManager;
import com.cycling_advocacy.bumpy.achievements.AchievementsViewModel;
import com.cycling_advocacy.bumpy.achievements.db.AchievementEntity;

import java.util.ArrayList;
import java.util.List;

public class AchievementsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AchievementsViewModel achievementsViewModel = ViewModelProviders
                .of(this).get(AchievementsViewModel.class);

        View root = inflater.inflate(R.layout.fragment_achievements, container, false);
        RecyclerView rv = root.findViewById(R.id.rv_achievements);

        final AchievementAdapter adapter = new AchievementAdapter(getContext());
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        achievementsViewModel.achievementsLiveData.observe(getViewLifecycleOwner(), achievementEntities -> {
            List<Achievement> achievementsJoined = new ArrayList<>();
            for(AchievementEntity entity : achievementEntities) {
                achievementsJoined.add(AchievementManager.convertToAchievement(entity));
            }

            adapter.setAchievementList(achievementsJoined);
            adapter.notifyDataSetChanged();
        });

        return root;
    }
}