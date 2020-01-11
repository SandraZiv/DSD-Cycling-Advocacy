package com.cycling_advocacy.bumpy.ui.past_trips;

import com.cycling_advocacy.bumpy.entities.PastTrip;

interface PastTripClickedListener {
    void onSyncClick(PastTrip pastTrip);
    void onPastTripLongClick(PastTrip pastTrip);
}
