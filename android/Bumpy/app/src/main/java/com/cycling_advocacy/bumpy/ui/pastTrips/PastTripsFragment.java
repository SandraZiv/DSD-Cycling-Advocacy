package com.cycling_advocacy.bumpy.ui.pastTrips;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.cycling_advocacy.bumpy.R;

public class PastTripsFragment extends Fragment {

    private PastTripsViewModel pastTripsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pastTripsViewModel = ViewModelProviders.of(this).get(PastTripsViewModel.class);

        View root = inflater.inflate(R.layout.fragment_past_trips, container, false);
        return root;
    }
}