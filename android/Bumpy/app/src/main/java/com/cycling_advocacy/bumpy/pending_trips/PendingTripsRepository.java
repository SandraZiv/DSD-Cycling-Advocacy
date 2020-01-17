package com.cycling_advocacy.bumpy.pending_trips;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.cycling_advocacy.bumpy.db.BumpyDB;

import java.util.List;
import java.util.concurrent.Executors;

class PendingTripsRepository {

    private final PendingTripsDao pendingTripsDao;
    private LiveData<List<PendingTrip>> pendingTrips;

    PendingTripsRepository(Context context) {
        this.pendingTripsDao = BumpyDB.getInstance(context).pendingTripsDao();
        this.pendingTrips = pendingTripsDao.getPendingTrips();
    }

    void insertAsync(final PendingTrip pendingTrip) {
        Executors.newSingleThreadExecutor().execute(() -> this.pendingTripsDao.insert(pendingTrip));
    }

    void deleteAsync(final PendingTrip pendingTrip) {
        Executors.newSingleThreadExecutor().execute(() -> this.pendingTripsDao.delete(pendingTrip));
    }

    void deleteByTripUUIDAsync(String tripUUID) {
        Executors.newSingleThreadExecutor().execute(() -> this.pendingTripsDao.deleteByTripUUID(tripUUID));
    }

    LiveData<List<PendingTrip>> getPendingTrips() {
        return this.pendingTrips;
    }

    LiveData<PendingTrip> getPendingTripByTripUUID(String tripUUID) {
        return this.pendingTripsDao.getPendingTripByTripUUID(tripUUID);
    }
}
