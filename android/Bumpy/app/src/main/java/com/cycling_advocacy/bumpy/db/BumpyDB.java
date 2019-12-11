package com.cycling_advocacy.bumpy.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cycling_advocacy.bumpy.achievements.db.AchievementsInitial;
import com.cycling_advocacy.bumpy.achievements.db.AchievementDao;
import com.cycling_advocacy.bumpy.achievements.Achievement;

import java.util.concurrent.Executors;

// todo add waiting trip entity
@Database(entities = {Achievement.class}, exportSchema = false, version = 1)
public abstract class BumpyDB extends RoomDatabase {

    private static final String dbName = "bumpy_db";
    private static BumpyDB INSTANCE = null;

    public static synchronized BumpyDB getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), BumpyDB.class, dbName)
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Executors.newSingleThreadExecutor().execute(new Runnable() {
                                @Override
                                public void run() {
                                    getInstance(context)
                                            .achievementDao()
                                            .insertAll(AchievementsInitial.getAchievements());
                                }
                            });
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return INSTANCE;
    }

    public abstract AchievementDao achievementDao();

    public abstract WaitingTripsDao waitingTripsDao();
}
