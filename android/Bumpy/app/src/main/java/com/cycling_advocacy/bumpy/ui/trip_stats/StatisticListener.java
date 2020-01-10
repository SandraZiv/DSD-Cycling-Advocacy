package com.cycling_advocacy.bumpy.ui.trip_stats;

import com.cycling_advocacy.bumpy.net.model.PastTripDetailedResponse;

public interface StatisticListener {
    void onStatisticDone(PastTripDetailedResponse statistics);
    void onError();
}
