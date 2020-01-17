package com.cycling_advocacy.bumpy.ui.map;

import com.cycling_advocacy.bumpy.net.model.BumpyPointsResponse;

import java.util.List;

public interface BumpyPointsListener {

    void onBumpyPointsObtained(List<BumpyPointsResponse> bumpyPoints);
}
