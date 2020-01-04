package com.cycling_advocacy.bumpy.pending_trips;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "pending_trips")
public class PendingTrip {

    @NotNull
    @PrimaryKey
    private String tripUUID;

    @ColumnInfo(name = "trip_serialized")
    private byte[] tripSerialized;


    public String getTripUUID() {
        return tripUUID;
    }

    public void setTripUUID(String tripUUID) {
        this.tripUUID = tripUUID;
    }

    public byte[] getTripSerialized() {
        return tripSerialized;
    }

    public void setTripSerialized(byte[] tripSerialized) {
        this.tripSerialized = tripSerialized;
    }
}
