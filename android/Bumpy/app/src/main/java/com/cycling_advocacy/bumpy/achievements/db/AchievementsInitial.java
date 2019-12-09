package com.cycling_advocacy.bumpy.achievements.db;

import com.cycling_advocacy.bumpy.R;
import com.cycling_advocacy.bumpy.achievements.Achievement;

public class AchievementsInitial {

    public static Achievement[] getAchievements() {
        return new Achievement[]{a1, a2};
    }

    private static Achievement a1 = new Achievement(
            R.string.a1_title,
            R.string.a1_detail,
            false
    );

    private static Achievement a2 = new Achievement(
            R.string.a2_title,
            R.string.a2_detail,
            false
    );

    // todo Izabella - add remaining achievements + update in res/values/achievements.xml
}
