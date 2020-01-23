package com.cycling_advocacy.bumpy.ui.past_trips;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cycling_advocacy.bumpy.R;
import com.cycling_advocacy.bumpy.achievements.util.AchievementsPrefs;
import com.cycling_advocacy.bumpy.entities.PastTrip;
import com.cycling_advocacy.bumpy.net.DataManager;
import com.cycling_advocacy.bumpy.net.DataRetriever;
import com.cycling_advocacy.bumpy.net.DataSender;
import com.cycling_advocacy.bumpy.net.OnDeleteTripListener;
import com.cycling_advocacy.bumpy.pending_trips.PendingTrip;
import com.cycling_advocacy.bumpy.pending_trips.PendingTripsViewModel;
import com.cycling_advocacy.bumpy.utils.GeneralUtil;

import java.util.ArrayList;
import java.util.List;

public class PastTripsFragment extends Fragment
        implements PastTripClickedListener, PastTripsReceivedListener, OnDeleteTripListener {

    private PendingTripsViewModel pendingTripsViewModel;
    private PastTripAdapter adapter;
    private RecyclerView rv;
    private TextView emptyView;
    private ProgressBar pbLoading;

    private List<PastTrip> pastTrips;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pendingTripsViewModel = ViewModelProviders.of(this).get(PendingTripsViewModel.class);

        View root = inflater.inflate(R.layout.fragment_past_trips, container, false);
        rv = root.findViewById(R.id.rv_past_trips);
        emptyView = root.findViewById(R.id.tv_empty_view);
        pbLoading = root.findViewById(R.id.pb_loading);

        adapter = new PastTripAdapter(getContext(), this);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        pendingTripsViewModel.pendingTripsLiveData.observe(getViewLifecycleOwner(), pendingTrips -> {
            pastTrips = new ArrayList<>();
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
    public void onResume() {
        super.onResume();
        startLoading();
        DataRetriever.getPastTripsList(getContext(), this);
    }

    private void startLoading() {
        rv.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        pbLoading.setVisibility(View.VISIBLE);
    }


    private void stopLoading() {
        pbLoading.setVisibility(View.GONE);
        rv.setVisibility(View.VISIBLE);
    }

    private void manageEmptyView() {
        if (adapter.getItemCount() == 0) {
            rv.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            rv.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPastTripsReceived(List<PastTrip> pastTrips) {
        adapter.addApiData(pastTrips);
        stopLoading();
        manageEmptyView();
    }

    @Override
    public void onPastTripsError() {
        stopLoading();
        manageEmptyView();
    }

    @Override
    public void onSyncClick(PastTrip pastTrip) {
        pendingTripsViewModel.getPendingTripByTripUUID(pastTrip.getTripUUID()).observe(this, pendingTrip -> {
            if (pendingTrip != null) {
                DataSender.sendPendingData(getContext(), PastTripsFragment.this, pendingTrip);
                DataRetriever.getPastTripsList(getContext(), this);
            }
        });
    }

    @Override
    public void onPastTripLongClick(PastTrip pastTrip) {
        Context ctx = getContext();
        if (ctx == null) {
            return;
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(ctx.getString(R.string.past_trip_title, GeneralUtil.formatTimestampLocale(pastTrip.getStartTime())));

        ArrayAdapter<String> optionList = new ArrayAdapter<>(ctx, android.R.layout.simple_list_item_1);
        optionList.add(getString(R.string.delete));

        builder.setAdapter(optionList, (dialog, which) -> {
            if (pastTrip.isUploaded()) {
                DataManager.deleteTrip(ctx, this, pastTrip.getTripUUID(), pastTrip.getStartTime());
            } else {
                pendingTripsViewModel.deleteByTripUUID(pastTrip.getTripUUID());
                AchievementsPrefs.decreaseDailyTripCount(ctx, pastTrip.getStartTime().getTime());
                AchievementsPrefs.decreaseTotalTripCount(ctx);
            }

            dialog.cancel();
        });

        builder.show();
    }

    @Override
    public void onDeleteSuccess() {
        DataRetriever.getPastTripsList(getContext(), this);
    }

    @Override
    public void onDeleteError() {
        Toast.makeText(getContext(), R.string.deleted_error, Toast.LENGTH_LONG).show();
    }
}