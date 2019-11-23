package com.cycling_advocacy.bumpy.ui.pastTrips;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PastTripsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PastTripsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is past trips fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}