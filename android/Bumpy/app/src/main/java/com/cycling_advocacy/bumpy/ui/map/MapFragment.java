package com.cycling_advocacy.bumpy.ui.map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.cycling_advocacy.bumpy.R;
import com.cycling_advocacy.bumpy.TripInProgressActivity;

public class MapFragment extends Fragment {

    private MapViewModel mapViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mapViewModel =
                ViewModelProviders.of(this).get(MapViewModel.class);
        View root = inflater.inflate(R.layout.fragment_map, container, false);


        final Button buttonStart = root.findViewById(R.id.button_start_trip);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activity2Intent = new Intent(getContext(), TripInProgressActivity.class);
                startActivity(activity2Intent);
            }
        });
        return root;
    }
}