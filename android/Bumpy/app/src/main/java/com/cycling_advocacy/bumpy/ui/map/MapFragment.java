package com.cycling_advocacy.bumpy.ui.map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cycling_advocacy.bumpy.R;
import com.cycling_advocacy.bumpy.TripInProgressActivity;
import com.cycling_advocacy.bumpy.entities.Trip;
import com.cycling_advocacy.bumpy.net.DataSender;
import com.cycling_advocacy.bumpy.utils.PermissionUtil;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;


public class MapFragment extends Fragment {

    public static final int REQ_CODE_TRIP_UPLOAD = 21021;
    public static final String EXTRA_TRIP = "EXTRA_TRIP";

    private Button buttonStart;
    private Switch gpsButton;
    private Context ctx;

    private MapView map = null;
    private MyLocationNewOverlay mLocationOverlay = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_map, container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ctx = getContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        map = root.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(15.5);
        GeoPoint startPoint = new GeoPoint(45.807323, 15.967772);
        mapController.setCenter(startPoint);
        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(ctx), map);
        GpsMyLocationProvider provider = new GpsMyLocationProvider(ctx);
        provider.addLocationSource(LocationManager.NETWORK_PROVIDER);
        mLocationOverlay.enableMyLocation();
        map.getOverlays().add(this.mLocationOverlay);

        ImageButton btCenterMap = root.findViewById(R.id.ic_center_map);
        btCenterMap.setOnClickListener(v -> {
            GeoPoint myPosition = new GeoPoint(mLocationOverlay.getLastFix());
            map.getController().animateTo(myPosition);
        });

        map.invalidate();

        buttonStart = root.findViewById(R.id.btn_start_trip);
        buttonStart.setOnClickListener(v -> {
            Intent intent = new Intent(ctx, TripInProgressActivity.class);
            startActivityForResult(intent, REQ_CODE_TRIP_UPLOAD);
        });

        gpsButton = root.findViewById(R.id.switch_gps);
        gpsButton.setOnClickListener(view -> {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);

            gpsButton.setChecked(!gpsButton.isChecked());
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (PermissionUtil.isLocationPermissionGranted(getContext())) {
            buttonStart.setEnabled(true);
        } else {
            buttonStart.setEnabled(false);
            Toast.makeText(getContext(), R.string.grant_location, Toast.LENGTH_LONG).show();
        }
        checkGpsStatus();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == REQ_CODE_TRIP_UPLOAD && resultCode == Activity.RESULT_OK) {
            Trip trip = (Trip) data.getSerializableExtra(EXTRA_TRIP);
            DataSender.sendData(getContext(), trip);
        }
    }

    private void checkGpsStatus() {
        LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        boolean gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        gpsButton.setChecked(gpsStatus);
    }
}