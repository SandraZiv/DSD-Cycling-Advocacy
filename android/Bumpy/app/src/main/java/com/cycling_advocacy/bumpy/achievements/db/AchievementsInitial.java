package com.cycling_advocacy.bumpy.achievements.db;

import com.cycling_advocacy.bumpy.R;
import com.cycling_advocacy.bumpy.achievements.Achievement;

public class AchievementsInitial {

    public static Achievement[] getAchievements() {
        return new Achievement[]{a1, a2, a3, a4, a5, a6, a7, a8};
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

    private static Achievement a3 = new Achievement(
            R.string.a3_title,
            R.string.a3_detail,
            false
    );

    private static Achievement a4 = new Achievement(
            R.string.a4_title,
            R.string.a4_detail,
            false
    );
    private static Achievement a5 = new Achievement(
            R.string.a5_title,
            R.string.a5_detail,
            false
    );

    private static Achievement a6 = new Achievement(
            R.string.a6_title,
            R.string.a6_detail,
            false
    );

    private static Achievement a7 = new Achievement(
            R.string.a7_title,
            R.string.a7_detail,
            false
    );

    private static Achievement a8 = new Achievement(
            R.string.a8_title,
            R.string.a8_detail,
            false
    );
}
