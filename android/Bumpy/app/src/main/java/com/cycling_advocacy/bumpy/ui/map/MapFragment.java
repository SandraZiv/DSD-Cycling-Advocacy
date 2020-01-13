package com.cycling_advocacy.bumpy.ui.map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cycling_advocacy.bumpy.BuildConfig;
import com.cycling_advocacy.bumpy.R;
import com.cycling_advocacy.bumpy.ui.TripInProgressActivity;
import com.cycling_advocacy.bumpy.entities.Trip;
import com.cycling_advocacy.bumpy.net.DataRetriever;
import com.cycling_advocacy.bumpy.net.DataSender;
import com.cycling_advocacy.bumpy.net.model.RoadQualitySegmentsResponse;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.DelayedMapListener;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;


public class MapFragment extends Fragment implements RoadQualityListener {

    private static final int REQ_CODE_TRIP_UPLOAD = 21021;
    public static final String EXTRA_TRIP = "EXTRA_TRIP";

    private Context ctx;

    private MapView map;
    private MyLocationNewOverlay mLocationOverlay;
    
    private static final double BAD_ROAD_QUALITY_THRESHOLD = 0.4;
    private static final double GOOD_ROAD_QUALITY_THRESHOLD = 0.6;

    private static final double MAP_ZOOM_DISPLAY_THRESHOLD = 15.5;

    private List<Polyline> currentlyDisplayedSegments = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_map, container, false);
        ctx = getContext();

        initMap(root);
        ImageButton btnCenterMap = root.findViewById(R.id.ic_center_map);
        btnCenterMap.setOnClickListener(v -> {
            if (checkGpsStatus()) {
                GeoPoint myPosition = mLocationOverlay.getMyLocation();
                if (myPosition != null) {
                    map.getController().animateTo(myPosition);
                    map.invalidate();
                }
            } else {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        Button btnStart = root.findViewById(R.id.btn_start_trip);
        btnStart.setOnClickListener(v -> {
            if (checkGpsStatus()) {
                Intent intent = new Intent(ctx, TripInProgressActivity.class);
                startActivityForResult(intent, REQ_CODE_TRIP_UPLOAD);
            } else {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        map.onResume();
        super.onResume();
        mLocationOverlay.enableMyLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
        mLocationOverlay.disableMyLocation();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == REQ_CODE_TRIP_UPLOAD && resultCode == Activity.RESULT_OK) {
            Trip trip = (Trip) data.getSerializableExtra(EXTRA_TRIP);
            DataSender.sendData(getContext(), this, trip);
        }
    }

    private boolean checkGpsStatus() {
        LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void initMap(View parent) {
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        Configuration.getInstance()
                .load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        map = parent.findViewById(R.id.map);

        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(ctx), map);
        map.getOverlays().add(mLocationOverlay);

        map.getTileProvider().clearTileCache();
        Configuration.getInstance().setCacheMapTileCount((short)16);
        Configuration.getInstance().setCacheMapTileOvershoot((short)16);
        Configuration.getInstance().setTileDownloadThreads((short)16);
        map.setTileSource(TileSourceFactory.MAPNIK);

        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();
        mapController.setZoom(15.5);

        // set start point
        GeoPoint startPoint = mLocationOverlay.getMyLocation();
        if (startPoint == null) {
            // Zagreb coordinated
            startPoint = new GeoPoint(45.815, 15.982);
        }
        mapController.setCenter(startPoint);
        map.invalidate();

        map.addMapListener(new DelayedMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                if (map.getZoomLevelDouble() >= MAP_ZOOM_DISPLAY_THRESHOLD) {
                    getRoadQualitySegments();
                }
                return true;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                if (event.getZoomLevel() >= MAP_ZOOM_DISPLAY_THRESHOLD) {
                    getRoadQualitySegments();
                } else {
                    // If map is too zoomed out we won't display road quality
                    // This Toast seems to not trigger sometimes
                    Toast.makeText(ctx, R.string.zoom_in_message, Toast.LENGTH_SHORT).show();
                    clearPolylines();
                }
                return true;
            }
        }));
    }

    private void getRoadQualitySegments() {
        BoundingBox boundingBox = map.getBoundingBox();

        double latNorth = boundingBox.getLatNorth();
        double latSouth = boundingBox.getLatSouth();
        double lonEast = boundingBox.getLonEast();
        double lonWest = boundingBox.getLonWest();

        DataRetriever.getRoadQualitySegments(ctx, this, latSouth, lonWest, latNorth, lonEast);
    }

    @Override
    public void onRoadQualitySegmentsObtained(List<RoadQualitySegmentsResponse> roadQualityData) {
        // Clear existing Polylines, drawing over each old segments could lead to too many lines and affect performance (?)
        clearPolylines();

        if (!roadQualityData.isEmpty()) {
            for (RoadQualitySegmentsResponse path : roadQualityData) {
                List<RoadQualitySegmentsResponse.Segment> segments = path.getSegments();
                for (RoadQualitySegmentsResponse.Segment segment : segments) {
                    Double quality = segment.getQualityScore();

                    int color;
                    if (quality == null) {
                        color = Color.BLACK;
                    } else if (quality < BAD_ROAD_QUALITY_THRESHOLD) {
                        color = Color.RED;
                    } else if (quality > GOOD_ROAD_QUALITY_THRESHOLD) {
                        color = Color.GREEN;
                    } else {
                        color = Color.YELLOW;
                    }

                    GeoPoint startPoint = new GeoPoint(segment.getStartLat(), segment.getStartLon());
                    GeoPoint endPoint = new GeoPoint(segment.getEndLat(), segment.getEndLon());
                    List<GeoPoint> segmentPoints = new ArrayList<>();
                    segmentPoints.add(startPoint);
                    segmentPoints.add(endPoint);

                    Polyline segmentLine = new Polyline();
                    segmentLine.setPoints(segmentPoints);
                    segmentLine.getOutlinePaint().setColor(color);
                    segmentLine.getOutlinePaint().setStrokeWidth(20);

                    map.getOverlayManager().add(segmentLine);
                    currentlyDisplayedSegments.add(segmentLine);
                }
            }

            map.invalidate();
        }
    }

    private void clearPolylines() {
        // TODO: Improve
        // I don't like keeping all displayed segments in a list since there could be a lot of them
        // I tried using map.getOverlayManager.clear() but that seems to remove the 'current position' guy as well and I wasn't able to re-draw him
        for (Polyline segment : currentlyDisplayedSegments) {
            map.getOverlayManager().remove(segment);
        }
    }
}