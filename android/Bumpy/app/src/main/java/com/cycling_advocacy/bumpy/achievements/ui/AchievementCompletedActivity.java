package com.cycling_advocacy.bumpy.achievements.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.cycling_advocacy.bumpy.R;
import com.cycling_advocacy.bumpy.achievements.Achievement;
import com.cycling_advocacy.bumpy.achievements.AchievementsViewModel;
import com.cycling_advocacy.bumpy.achievements.db.AchievementEntity;
import com.cycling_advocacy.bumpy.achievements.util.AchievementManager;

import java.util.List;

public class AchievementCompletedActivity extends AppCompatActivity {

    public static final String EXTRA_COMPLETED_ACHIEVEMENTS = "EXTRA_COMPLETED_ACHIEVEMENTS";
    private Context context;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement_completed);

        List<Achievement> completedAchievements =
                (List<Achievement>) getIntent().getSerializableExtra(EXTRA_COMPLETED_ACHIEVEMENTS);

        // todo change this to match original mockup
        String achievementCompleted =
                getResources().getQuantityString(R.plurals.achievement_completed,completedAchievements.size());

            ((TextView)findViewById(R.id.tv_achievement_completed_title))
                .setText(completedAchievements.size() + " " + achievementCompleted);

        // update DB
        AchievementsViewModel achievementsViewModel = ViewModelProviders
                .of(this).get(AchievementsViewModel.class);

        AchievementEntity[] entitiesToUpdate = new AchievementEntity[completedAchievements.size()];
        for (int i = 0; i < completedAchievements.size(); i++) {
            entitiesToUpdate[i] = AchievementManager.convertToEntity(completedAchievements.get(i));
        }
        achievementsViewModel.updateAll(entitiesToUpdate);

        new Handler().postDelayed(this::finish, 5000);
    }
}
