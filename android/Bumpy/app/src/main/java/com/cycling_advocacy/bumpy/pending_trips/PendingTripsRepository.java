package com.cycling_advocacy.bumpy.pending_trips;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.cycling_advocacy.bumpy.db.BumpyDB;

import java.util.List;
import java.util.concurrent.Executors;

public class PendingTripsRepository {

    private final PendingTripsDao pendingTripsDao;
    private LiveData<List<PendingTrip>> pendingTrips;

    public PendingTripsRepository(Context context) {
        this.pendingTripsDao = BumpyDB.getInstance(context).pendingTripsDao();
        this.pendingTrips = pendingTripsDao.getPendingTrips();
    }

    public void insertAsync(final PendingTrip pendingTrip) {
        Executors.newSingleThreadExecutor().execute(() -> this.pendingTripsDao.insert(pendingTrip));
    }

    public void deleteAsync(final PendingTrip pendingTrip) {
        Executors.newSingleThreadExecutor().execute(() -> this.pendingTripsDao.delete(pendingTrip));
    }

    public LiveData<List<PendingTrip>> getPendingTrips() {
        return this.pendingTrips;
    }
}
