package com.cycling_advocacy.bumpy.ui.trip_stats;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cycling_advocacy.bumpy.BuildConfig;
import com.cycling_advocacy.bumpy.R;
import com.cycling_advocacy.bumpy.entities.GnssData;
import com.cycling_advocacy.bumpy.net.DataRetriever;
import com.cycling_advocacy.bumpy.net.model.PastTripDetailedResponse;
import com.cycling_advocacy.bumpy.utils.GeneralUtil;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

public class PastTripStatisticsActivity extends AppCompatActivity implements StatisticListener {

    public static final String EXTRA_TRIP_UUID = "tripUUID";

    private TextView tvTripStatUUID;
    private TextView tvTripStatStartTS;
    private TextView tvTripStatEndTS;
    private TextView tvTripStatDuration;
    private TextView tvTripStatDistance;
    private TextView tvTripStatMaxSpeed;
    private TextView tvTripStatAvgSpeed;
    private TextView tvTripStatMinElevation;
    private TextView tvTripStatMaxElevation;
    private TextView tvTripStatAvgElevation;

    private MapView routeMap;

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

        tvTripStatUUID = findViewById(R.id.tv_trip_stat_uuid);
        tvTripStatStartTS = findViewById(R.id.tv_trip_stat_start_ts);
        tvTripStatEndTS = findViewById(R.id.tv_trip_stat_end_ts);
        tvTripStatDuration = findViewById(R.id.tv_trip_stat_duration);
        tvTripStatDistance = findViewById(R.id.tv_trip_stat_distance);
        tvTripStatMaxSpeed = findViewById(R.id.tv_trip_stat_max_speed);
        tvTripStatAvgSpeed = findViewById(R.id.tv_trip_stat_avg_speed);
        tvTripStatMinElevation = findViewById(R.id.tv_trip_stat_min_elevation);
        tvTripStatMaxElevation = findViewById(R.id.tv_trip_stat_max_elevation);
        tvTripStatAvgElevation = findViewById(R.id.tv_trip_stat_avg_elevation);

        DataRetriever.getPastTripStatistics(this, this, getIntent().getStringExtra(EXTRA_TRIP_UUID));

        initRouteMap();
    }

    @Override
    public void onStatisticDone(PastTripDetailedResponse statistics) {
        tvTripStatUUID.setText(getString(R.string.trip_stat_uuid, statistics.getTripUUID()));

        if (statistics.getStartTS() != null) {
            tvTripStatStartTS.setText(getString(R.string.trip_stat_start_ts, statistics.getStartTS().toString()));
        }

        if (statistics.getEndTS() != null) {
            tvTripStatEndTS.setText(getString(R.string.trip_stat_end_ts, statistics.getEndTS().toString()));
        }

        if (statistics.getStartTS() != null && statistics.getEndTS() != null) {
            String duration = GeneralUtil.formatDuration(statistics.getStartTS(), statistics.getEndTS());
            tvTripStatDuration.setText(getString(R.string.trip_stat_duration, duration));
        }

        tvTripStatDistance.setText(getString(R.string.trip_stat_distance, statistics.getDistance()));

        if (statistics.getSpeed() != null) {
            PastTripDetailedResponse.Speed speed = statistics.getSpeed();
            tvTripStatMaxSpeed.setText(getString(R.string.trip_stat_max_speed, speed.getMaxSpeed()));
            tvTripStatAvgSpeed.setText(getString(R.string.trip_stat_avg_speed, speed.getAvgSpeed()));
        }

        if (statistics.getElevation() != null) {
            PastTripDetailedResponse.Elevation elevation = statistics.getElevation();
            tvTripStatMinElevation.setText(getString(R.string.trip_stat_min_elevation, elevation.getMinElevation()));
            tvTripStatMaxElevation.setText(getString(R.string.trip_stat_max_elevation, elevation.getMaxElevation()));
            tvTripStatAvgElevation.setText(getString(R.string.trip_stat_avg_elevation, elevation.getAvgElevation()));
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
                // How to set this. This value is simply one that seemed to initially look fine.
                route.getOutlinePaint().setStrokeWidth(20);

                routeMap.getOverlayManager().add(route);
                GeoPoint startPoint = new GeoPoint(gnssPoints.get(0).getLat(), gnssPoints.get(0).getLon());
                routeMap.getController().setCenter(startPoint);
                routeMap.invalidate();
            } else {
                Toast.makeText(this, R.string.trip_stat_gnss_empty_message, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.trip_stat_gnss_null_message, Toast.LENGTH_SHORT).show();
        }
    }

    public void initRouteMap() {
        Context ctx = this;

        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        Configuration.getInstance()
                .load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        routeMap = findViewById(R.id.mv_route_map);

        // Config set up like MapFragment. Should it be different?
        routeMap.getTileProvider().clearTileCache();
        Configuration.getInstance().setCacheMapTileCount((short)16);
        Configuration.getInstance().setCacheMapTileOvershoot((short)16);
        Configuration.getInstance().setTileDownloadThreads((short)16);
        routeMap.setTileSource(TileSourceFactory.MAPNIK);

        routeMap.setMultiTouchControls(true);

        IMapController mapController = routeMap.getController();
        // How to set this. This value is simply one that seemed to initially look fine.
        mapController.setZoom(17.5);
        routeMap.invalidate();
    }
}