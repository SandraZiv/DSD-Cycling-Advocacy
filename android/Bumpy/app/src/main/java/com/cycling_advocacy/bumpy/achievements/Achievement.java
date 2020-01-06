package com.cycling_advocacy.bumpy.achievements;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import java.io.Serializable;

public class Achievement implements Serializable {

    private int id;

    @StringRes
    private int titleId;

    @StringRes
    private int detailId;

    private boolean isCompleted;

    private AchievementCondition condition;

    // using id so it can support internationalization
    public Achievement(
            int id,
            @StringRes int titleId,
            @StringRes int detailId,
            boolean isCompleted,
            AchievementCondition condition
    ) {
        this.id = id;
        this.titleId = titleId;
        this.detailId = detailId;
        this.isCompleted = isCompleted;
        this.condition = condition;
    }


    public int getId() {
        return id;
    }

    public int getTitleId() {
        return titleId;
    }

    public int getDetailId() {
        return detailId;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public AchievementCondition getCondition() {
        return condition;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Achievement)) {
            return false;
        }
        return this.getId() == ((Achievement)obj).getId();
    }
}
