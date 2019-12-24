package com.cycling_advocacy.bumpy.ui.pastTrips;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cycling_advocacy.bumpy.entities.PastTrip;
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
        List<PastTrip> testList = new ArrayList<>();
        testList.add(new PastTrip("A", "B", "0", false));
        pastTripsLiveData.setValue(testList);
    }

    public LiveData<List<PastTrip>> getPastTrips() {
        return pastTripsLiveData;
    }

    public void updatePastTrips() {
        // TODO: Make call to API and set pastTripsLiveData with new values?
        // This should cause an update in the PastTripsFragment observer which should cause an update in the adapter (?)
    }
}