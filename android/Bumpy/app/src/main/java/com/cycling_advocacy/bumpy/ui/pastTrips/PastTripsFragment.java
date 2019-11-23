package com.cycling_advocacy.bumpy.ui.pastTrips;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cycling_advocacy.bumpy.R;
import com.cycling_advocacy.bumpy.ui.reportIssue.ReportIssueViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class PastTripsFragment extends Fragment {

    private PastTripsViewModel pastTripsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pastTripsViewModel =
                ViewModelProviders.of(this).get(PastTripsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_past_trips, container, false);
        final TextView textView = root.findViewById(R.id.text_pastTrips);
        pastTripsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}