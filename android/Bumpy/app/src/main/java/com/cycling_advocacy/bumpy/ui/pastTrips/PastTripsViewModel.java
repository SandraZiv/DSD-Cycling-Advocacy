package com.cycling_advocacy.bumpy.ui.pastTrips;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cycling_advocacy.bumpy.entities.PastTrip;
import com.cycling_advocacy.bumpy.net.DataRetriever;
import com.cycling_advocacy.bumpy.utils.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class PastTripsViewModel extends AndroidViewModel {

    public MutableLiveData<List<PastTrip>> pastTripsLiveData;

    public PastTripsViewModel(Application application) {
        super(application);
        pastTripsLiveData = new MutableLiveData<>();

        String deviceUUID = PreferenceUtil.getDeviceUUID(application.getApplicationContext());

        // TODO: Remove test list
        /*List<PastTrip> testList = new ArrayList<>();
        testList.add(new PastTrip("A", "B", "0", false));
        pastTripsLiveData.setValue(testList);*/
        updatePastTrips(application.getApplicationContext());
    }

    public LiveData<List<PastTrip>> getPastTrips() {
        return pastTripsLiveData;
    }

    public void setPastTripsLiveData(List<PastTrip> pastTrips) {
        pastTripsLiveData.setValue(pastTrips);
    }

    public void updatePastTrips(Context context) {
        // TODO: Make call to API and set pastTripsLiveData with new values?
        // This should cause an update in the PastTripsFragment observer which should cause an update in the adapter (?)

        DataRetriever.updatePastTripsList(context, this);
    }
}