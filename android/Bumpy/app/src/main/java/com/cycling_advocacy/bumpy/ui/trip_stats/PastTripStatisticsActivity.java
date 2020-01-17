package com.cycling_advocacy.bumpy.ui.trip_stats;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cycling_advocacy.bumpy.BuildConfig;
import com.cycling_advocacy.bumpy.R;
import com.cycling_advocacy.bumpy.entities.GnssData;
import com.cycling_advocacy.bumpy.net.DataManager;
import com.cycling_advocacy.bumpy.net.DataRetriever;
import com.cycling_advocacy.bumpy.net.OnDeleteTripListener;
import com.cycling_advocacy.bumpy.net.model.PastTripDetailedResponse;
import com.cycling_advocacy.bumpy.utils.GeneralUtil;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlay;
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlayOptions;
import org.osmdroid.views.overlay.simplefastpoint.SimplePointTheme;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PastTripStatisticsActivity extends AppCompatActivity
        implements StatisticListener, OnDeleteTripListener {

    public static final String EXTRA_TRIP_UUID = "tripUUID";
    private static final int REQ_CODE_EXPORT_CSV = 820;

    private TextView tvTripStatStartTS;
    private TextView tvTripStatDuration;
    private TextView tvTripStatDistance;
    private TextView tvTripStatMaxSpeed;
    private TextView tvTripStatAvgSpeed;
    private TextView tvTripStatMinElevation;
    private TextView tvTripStatMaxElevation;
    private TextView tvTripStatAvgElevation;
    private TextView tvTripAvgVibration;
    private TextView tvTripBumpsDetection;

    private MapView routeMap;

    private String tripUUID = "";
    private Date tripStartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_trip_statistics);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> finish());

        tvTripStatStartTS = findViewById(R.id.tv_start);
        tvTripStatDuration = findViewById(R.id.tv_duration_value);
        tvTripStatDistance = findViewById(R.id.tv_distance_value);
        tvTripStatMaxSpeed = findViewById(R.id.tv_max_speed_value);
        tvTripStatAvgSpeed = findViewById(R.id.tv_avg_speed_value);
        tvTripStatMinElevation = findViewById(R.id.tv_min_elevation_value);
        tvTripStatMaxElevation = findViewById(R.id.tv_max_elevation_value);
        tvTripStatAvgElevation = findViewById(R.id.tv_avg_elevation_value);
        tvTripAvgVibration = findViewById(R.id.tv_avg_vibration_value);
        tvTripBumpsDetection = findViewById(R.id.tv_bumps_detected_value);

        DataRetriever.getPastTripStatistics(this, this, getIntent().getStringExtra(EXTRA_TRIP_UUID));

        initRouteMap();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.trip_stats_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionDelete:
                deleteTrip();
                return true;
            case R.id.actionExportMotion:
                exportMotionFile();
                return true;
        }

        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == REQ_CODE_EXPORT_CSV && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                Uri uri = resultData.getData();
                DataManager.getMotionFile(this, this.tripUUID, uri);
            }
        }
    }

    @Override
    public void onStatisticDone(PastTripDetailedResponse statistics) {
        this.tripUUID = statistics.getTripUUID();
        this.tripStartTime = statistics.getStartTS();

        if (statistics.getStartTS() != null) {
            tvTripStatStartTS.setText(GeneralUtil.formatTimestampLocale(statistics.getStartTS()));
        }

        if (statistics.getStartTS() != null && statistics.getEndTS() != null) {
            String duration = GeneralUtil.formatDuration(statistics.getStartTS(), statistics.getEndTS());
            tvTripStatDuration.setText(duration);
        }

        tvTripStatDistance.setText(GeneralUtil.formatDecimal(statistics.getDistance()));

        if (statistics.getSpeed() != null) {
            PastTripDetailedResponse.Speed speed = statistics.getSpeed();
            tvTripStatMaxSpeed.setText(GeneralUtil.formatDecimal(speed.getMaxSpeed()));
            tvTripStatAvgSpeed.setText(GeneralUtil.formatDecimal(speed.getAvgSpeed()));
        }

        if (statistics.getElevation() != null) {
            PastTripDetailedResponse.Elevation elevation = statistics.getElevation();
            tvTripStatMinElevation.setText(GeneralUtil.formatNoDecimal(elevation.getMinElevation()));
            tvTripStatMaxElevation.setText(GeneralUtil.formatNoDecimal(elevation.getMaxElevation()));
            tvTripStatAvgElevation.setText(GeneralUtil.formatNoDecimal(elevation.getAvgElevation()));
        }

        if (statistics.getVibration() != null) {
            tvTripAvgVibration.setText(GeneralUtil.formatDecimal(statistics.getVibration().getAvgVibration()));
        }

        if (statistics.getGnssData() != null) {
            if (!statistics.getGnssData().isEmpty()) {
                List<GnssData> gnssPoints = statistics.getGnssData();
                List<GeoPoint> routePoints = new ArrayList<>();
                for (GnssData point : gnssPoints) {
                    routePoints.add(new GeoPoint(point.getLat(), point.getLon()));
                }

                Polyline route = new Polyline();
                route.setPoints(routePoints);
                route.getOutlinePaint().setColor(Color.RED);
                route.getOutlinePaint().setStrokeWidth(20);

                Marker startMarker = new Marker(routeMap);
                startMarker.setPosition(routePoints.get(0));
                startMarker.setIcon(getResources().getDrawable(R.drawable.ic_start_icon));
                startMarker.setAnchor(0.5f,0.5f);
                startMarker.setTitle(getString(R.string.trip_stats_marker_start, GeneralUtil.formatTimestampLocale(statistics.getStartTS())));


                Marker endMarker = new Marker(routeMap);
                endMarker.setPosition(routePoints.get(routePoints.size()-1));
                endMarker.setIcon(getResources().getDrawable(R.drawable.ic_final));
                endMarker.setAnchor(0.5f,0.5f);
                endMarker.setTitle(getString(R.string.trip_stats_marker_end, GeneralUtil.formatTimestampLocale(statistics.getEndTS())));

                routeMap.getOverlayManager().add(route);
                routeMap.getController().setCenter(routePoints.get(0));
                routeMap.getOverlays().add(startMarker);
                routeMap.getOverlays().add(endMarker);
                routeMap.invalidate();
            } else {
                Toast.makeText(this, R.string.trip_stat_gnss_empty_message, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.trip_stat_gnss_null_message, Toast.LENGTH_SHORT).show();
        }

        if (statistics.getBumpyPoints() != null) {
            tvTripBumpsDetection.setText(GeneralUtil.formatInt(statistics.getBumpyPoints().size()));

            if (!statistics.getBumpyPoints().isEmpty()) {
                List<IGeoPoint> bumpyPoints = new ArrayList<>();
                for (PastTripDetailedResponse.BumpyPoint point : statistics.getBumpyPoints()) {
                    bumpyPoints.add(new GeoPoint(point.getLat(), point.getLon()));
                }

                SimplePointTheme pointTheme = new SimplePointTheme(bumpyPoints, false);

                SimpleFastPointOverlayOptions options = SimpleFastPointOverlayOptions.getDefaultStyle()
                        .setAlgorithm(SimpleFastPointOverlayOptions.RenderingAlgorithm.MAXIMUM_OPTIMIZATION)
                        .setRadius(7);

                final SimpleFastPointOverlay pointOverlay = new SimpleFastPointOverlay(pointTheme, options);

                routeMap.getOverlays().add(pointOverlay);
            } else {
                Toast.makeText(this, R.string.trip_stat_bump_points_empty_message, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.trip_stat_bump_points_null_message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStatisticError() {
        finish();
    }

    private void initRouteMap() {
        Context ctx = this;

        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        Configuration.getInstance()
                .load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        routeMap = findViewById(R.id.mv_route_map);

        routeMap.getTileProvider().clearTileCache();
        Configuration.getInstance().setCacheMapTileCount((short) 16);
        Configuration.getInstance().setCacheMapTileOvershoot((short) 16);
        Configuration.getInstance().setTileDownloadThreads((short) 16);
        routeMap.setTileSource(TileSourceFactory.MAPNIK);

        routeMap.setMultiTouchControls(true);

        IMapController mapController = routeMap.getController();
        mapController.setZoom(15.4);
        routeMap.invalidate();
    }

    private void deleteTrip() {
        DataManager.deleteTrip(this, this, this.tripUUID, this.tripStartTime);
    }

    private void exportMotionFile() {
        openDocumentSavePicker();
    }

    private void openDocumentSavePicker() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/csv");
        intent.putExtra(Intent.EXTRA_TITLE, this.tripUUID + ".csv");

        startActivityForResult(intent, REQ_CODE_EXPORT_CSV);
    }

    @Override
    public void onDeleteSuccess() {
        finish();
    }

    @Override
    public void onDeleteError() {
        Toast.makeText(this, R.string.deleted_error, Toast.LENGTH_LONG).show();
    }
}