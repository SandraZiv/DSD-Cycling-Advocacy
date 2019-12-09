package com.cycling_advocacy.bumpy.achievements;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cycling_advocacy.bumpy.achievements.db.AchievementRepository;

import java.util.List;

public class AchievementsViewModel extends AndroidViewModel {

    private AchievementRepository repository;
    public LiveData<List<Achievement>> achievementsLiveData;

    public AchievementsViewModel(Application application) {
        super(application);

        repository = new AchievementRepository(application);
        achievementsLiveData = repository.getAchievements();
    }

    public void update(Achievement achievement) {
        repository.updateAsync(achievement);
    }
}