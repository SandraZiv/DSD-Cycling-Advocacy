package com.cycling_advocacy.bumpy.achievements.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.cycling_advocacy.bumpy.R;
import com.cycling_advocacy.bumpy.achievements.Achievement;
import com.cycling_advocacy.bumpy.achievements.util.AchievementManager;
import com.cycling_advocacy.bumpy.achievements.AchievementsViewModel;
import com.cycling_advocacy.bumpy.achievements.db.AchievementEntity;

import java.util.List;

public class AchievementCompletedActivity extends AppCompatActivity {

    public static final String EXTRA_COMPLETED_ACHIEVEMENTS = "EXTRA_COMPLETED_ACHIEVEMENTS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement_completed);

        // todo Set instead?
        List<Achievement> completedAchievements =
                (List<Achievement>) getIntent().getSerializableExtra(EXTRA_COMPLETED_ACHIEVEMENTS);

        // todo change this to match original mockup
        ((TextView)findViewById(R.id.tv_achievement_completed_title))
                .setText("You just completed " + completedAchievements.size() + " achievements");

        // update DB
        AchievementsViewModel achievementsViewModel = ViewModelProviders
                .of(this).get(AchievementsViewModel.class);

        AchievementEntity[] entitiesToUpdate = new AchievementEntity[completedAchievements.size()];
        for (int i = 0; i < completedAchievements.size(); i++) {
            entitiesToUpdate[i] = AchievementManager.convertToEntity(completedAchievements.get(i));
        }
        achievementsViewModel.updateAll(entitiesToUpdate);


    }
}
