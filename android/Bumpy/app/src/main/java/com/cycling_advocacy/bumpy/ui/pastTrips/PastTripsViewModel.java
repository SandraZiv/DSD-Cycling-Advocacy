package com.cycling_advocacy.bumpy.ui.pastTrips;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cycling_advocacy.bumpy.entities.PastTrip;
import com.cycling_advocacy.bumpy.net.DataRetriever;

import java.util.List;

public class PastTripsViewModel extends AndroidViewModel {

    public MutableLiveData<List<PastTrip>> pastTripsLiveData;

    public PastTripsViewModel(Application application) {
        super(application);
        pastTripsLiveData = new MutableLiveData<>();

        updatePastTrips(application.getApplicationContext());
    }

    public LiveData<List<PastTrip>> getPastTrips() {
        return pastTripsLiveData;
    }

    public void setPastTripsLiveData(List<PastTrip> pastTrips) {
        pastTripsLiveData.setValue(pastTrips);
    }

    public void updatePastTrips(Context context) {
        DataRetriever.updatePastTripsList(context, this);
    }
}