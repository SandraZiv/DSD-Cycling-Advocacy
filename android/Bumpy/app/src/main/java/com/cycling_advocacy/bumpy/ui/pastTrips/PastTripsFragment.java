package com.cycling_advocacy.bumpy.ui.pastTrips;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cycling_advocacy.bumpy.R;
import com.cycling_advocacy.bumpy.entities.PastTrip;
import com.cycling_advocacy.bumpy.net.DataRetriever;

import java.util.List;

public class PastTripsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PastTripsViewModel pastTripsViewModel = ViewModelProviders.of(this).get(PastTripsViewModel.class);

        View root = inflater.inflate(R.layout.fragment_past_trips, container, false);
        RecyclerView rv = root.findViewById(R.id.rv_past_trips);

        final PastTripAdapter adapter = new PastTripAdapter(getContext());
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        pastTripsViewModel.pastTripsLiveData.observe(this, new Observer<List<PastTrip>>() {
            @Override
            public void onChanged(List<PastTrip> pastTripList) {
                adapter.setPastTripList(pastTripList);
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        PastTripsViewModel pastTripsViewModel = ViewModelProviders.of(this).get(PastTripsViewModel.class);
        DataRetriever.getPastTripsList(getContext(), pastTripsViewModel);
    }
}