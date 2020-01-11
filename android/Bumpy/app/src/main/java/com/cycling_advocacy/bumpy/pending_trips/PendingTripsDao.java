package com.cycling_advocacy.bumpy.pending_trips;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PendingTripsDao {
    // define methods that access the db

    @Query("Select * from pending_trips order by tripUUID")
    LiveData<List<PendingTrip>> getPendingTrips();

    @Query("Select * from pending_trips where tripUUID = :tripUUID")
    LiveData<PendingTrip> getPendingTripByTripUUID(String tripUUID);

    @Insert
    void insert(PendingTrip pendingTrip);

    @Delete
    void delete(PendingTrip pendingTrip);

    @Query("DELETE FROM pending_trips WHERE tripUUID = :tripUUID")
    void deleteByTripUUID(String tripUUID);
}
