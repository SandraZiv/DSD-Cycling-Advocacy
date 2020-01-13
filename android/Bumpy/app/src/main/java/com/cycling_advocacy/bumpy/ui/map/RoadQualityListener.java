package com.cycling_advocacy.bumpy.ui.map;

import com.cycling_advocacy.bumpy.net.model.RoadQualitySegmentsResponse;

import java.util.List;

public interface RoadQualityListener {

    void onRoadQualitySegmentsObtained(List<RoadQualitySegmentsResponse> roadQualityData);
}
