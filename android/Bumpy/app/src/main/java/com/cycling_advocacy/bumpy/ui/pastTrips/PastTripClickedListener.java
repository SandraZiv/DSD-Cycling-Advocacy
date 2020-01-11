package com.cycling_advocacy.bumpy.ui.pastTrips;

import com.cycling_advocacy.bumpy.entities.PastTrip;

interface PastTripClickedListener {
    void upload(PastTrip pastTrip);
    void delete(PastTrip pastTrip);
}
