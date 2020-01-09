package com.cycling_advocacy.bumpy.ui.pastTrips;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.cycling_advocacy.bumpy.entities.PastTrip;
import com.cycling_advocacy.bumpy.net.DataRetriever;

import java.util.List;

public class PastTripsViewModel extends AndroidViewModel {

    public MutableLiveData<List<PastTrip>> pastTripsLiveData;

    public PastTripsViewModel(Application application) {
        super(application);
        this.pastTripsLiveData = new MutableLiveData<>();

        DataRetriever.getPastTripsList(application, this);
    }
}