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
import com.cycling_advocacy.bumpy.net.DataSender;
import com.cycling_advocacy.bumpy.pending_trips.PendingTrip;
import com.cycling_advocacy.bumpy.pending_trips.PendingTripsViewModel;

import java.util.ArrayList;
import java.util.List;

public class PastTripsFragment extends Fragment implements PastTripsUploadListener {

    private PendingTripsViewModel pendingTripsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PastTripsViewModel pastTripsViewModel = ViewModelProviders.of(this).get(PastTripsViewModel.class);
        pendingTripsViewModel = ViewModelProviders.of(this).get(PendingTripsViewModel.class);

        View root = inflater.inflate(R.layout.fragment_past_trips, container, false);
        RecyclerView rv = root.findViewById(R.id.rv_past_trips);

        final PastTripAdapter adapter = new PastTripAdapter(getContext(), this);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        pastTripsViewModel.pastTripsLiveData.observe(this, adapter::addApiData);

        pendingTripsViewModel.pendingTripsLiveData.observe(this, pendingTrips -> {
            List<PastTrip> pastTrips = new ArrayList<>();
            for (PendingTrip pendingTrip : pendingTrips) {
                PastTrip pastTrip = new PastTrip(pendingTrip);
                if (pastTrip.getTripUUID() != null) {
                    pastTrips.add(pastTrip);
                }
            }

            adapter.addDbData(pastTrips);
        });

        return root;
    }

    @Override
    public void upload(String tripUUID) {
        pendingTripsViewModel.getPendingTripByTripUUID(tripUUID).observe(this, new Observer<PendingTrip>() {
            @Override
            public void onChanged(PendingTrip pendingTrip) {
                if (pendingTrip != null) {
                    DataSender.sendPendingData(getContext(), PastTripsFragment.this, pendingTrip);
                }
            }
        });
    }
}