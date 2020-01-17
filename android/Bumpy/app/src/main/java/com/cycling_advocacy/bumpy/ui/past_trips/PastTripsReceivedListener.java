package com.cycling_advocacy.bumpy.ui.past_trips;

import com.cycling_advocacy.bumpy.entities.PastTrip;

import java.util.List;

public interface PastTripsReceivedListener {
    void onPastTripsReceived(List<PastTrip> pastTrips);
    void onPastTripsError();
}
