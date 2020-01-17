package com.cycling_advocacy.bumpy.achievements;

import android.content.Context;
import com.cycling_advocacy.bumpy.entities.Trip;

import java.io.Serializable;

public interface AchievementCondition extends Serializable {

    default boolean isCompleted(Trip trip) {
        return false;
    }

    default boolean isCompleted(Context context) {
        return false;
    }
}