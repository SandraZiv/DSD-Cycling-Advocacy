package com.cycling_advocacy.bumpy.ui.pastTrips;

import com.cycling_advocacy.bumpy.entities.PastTrip;

import java.util.List;

public interface PastTripsReceivedListener {
    void onReceived(List<PastTrip> pastTrips);
}
