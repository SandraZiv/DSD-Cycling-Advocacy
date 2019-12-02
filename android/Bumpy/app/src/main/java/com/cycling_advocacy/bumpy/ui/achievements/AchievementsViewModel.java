package com.cycling_advocacy.bumpy.ui.achievements;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AchievementsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AchievementsViewModel() {
        mText = new MutableLiveData<>();

    }

    public LiveData<String> getText() {
        return mText;
    }
}