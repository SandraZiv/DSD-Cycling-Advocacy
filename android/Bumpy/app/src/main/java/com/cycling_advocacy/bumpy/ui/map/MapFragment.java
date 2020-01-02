package com.cycling_advocacy.bumpy.ui.map;

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
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cycling_advocacy.bumpy.R;
import com.cycling_advocacy.bumpy.TripInProgressActivity;
import com.cycling_advocacy.bumpy.utils.PermissionUtil;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;


public class MapFragment extends Fragment {

    private Button buttonStart;
    private Switch gpsButton;
    private LocationManager locationManager;
    Context ctx;

    private MapView map = null;
    private MyLocationNewOverlay mLocationOverlay = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_map, container, false);
     
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ctx = getActivity().getApplicationContext();
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
        btCenterMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeoPoint myPosition = new GeoPoint(mLocationOverlay.getLastFix());
                map.getController().animateTo(myPosition);
            }
        });

        map.invalidate();

        buttonStart = root.findViewById(R.id.btn_start_trip);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activity2Intent = new Intent(getContext(), TripInProgressActivity.class);
                startActivity(activity2Intent);
            }
        });

        gpsButton = root.findViewById(R.id.switch_gps);
        gpsButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
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
            Toast.makeText(getContext(), R.string.grant_location , Toast.LENGTH_LONG).show();
        }
        checkGpsStatus();
    }

    public void checkGpsStatus(){
        locationManager = (LocationManager)ctx.getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        boolean gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(gpsStatus == true) {
            gpsButton.setChecked(true);
        } else {
            gpsButton.setChecked(false);
        }
    }
}