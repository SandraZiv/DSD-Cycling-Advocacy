package com.cycling_advocacy.bumpy.ui.past_trips;

import com.cycling_advocacy.bumpy.entities.PastTrip;

import java.util.List;

public interface PastTripsReceivedListener {
    void onReceived(List<PastTrip> pastTrips);
    void onError();
}
