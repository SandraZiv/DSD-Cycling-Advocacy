package com.cycling_advocacy.bumpy.net;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.cycling_advocacy.bumpy.R;
import com.cycling_advocacy.bumpy.ui.pastTrips.PastTripsReceivedListener;
import com.cycling_advocacy.bumpy.ui.trip_stats.StatisticListener;
import com.cycling_advocacy.bumpy.entities.PastTrip;
import com.cycling_advocacy.bumpy.net.model.PastTripDetailedResponse;
import com.cycling_advocacy.bumpy.net.model.PastTripGeneralResponse;
import com.cycling_advocacy.bumpy.net.service.BumpyService;
import com.cycling_advocacy.bumpy.net.service.BumpyServiceBuilder;
import com.cycling_advocacy.bumpy.utils.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class DataRetriever {

    public static void getPastTripsList(final Context context, PastTripsReceivedListener listener) {
        final String deviceUUID = PreferenceUtil.getLongDeviceUUID(context);
        BumpyService bumpyService = BumpyServiceBuilder.createService(BumpyService.class);
        bumpyService.getTripsByDeviceUUID(deviceUUID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<List<PastTripGeneralResponse>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        //Do nothing
                    }

                    @Override
                    public void onSuccess(Response<List<PastTripGeneralResponse>> response) {
                        Log.d("Get trips for device", "Get trips for device response: " + response.message());
                        if (!response.isSuccessful()) {
                            Toast.makeText(context, R.string.get_trips_not_successful, Toast.LENGTH_SHORT).show();
                            listener.onError();
                        } else {
                            List<PastTripGeneralResponse> pastTripsGeneral = response.body();
                            List<PastTrip> pastTrips = new ArrayList<>();
                            for (PastTripGeneralResponse pastTripGeneral : pastTripsGeneral) {
                                pastTrips.add(new PastTrip(pastTripGeneral));
                            }

                            listener.onReceived(pastTrips);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("Get trips for device", "Failed to retrieve trips for device: "  + e.getMessage());
                        Toast.makeText(context, R.string.get_trips_not_successful, Toast.LENGTH_SHORT).show();
                        listener.onError();
                    }
                });
    }

    public static void getPastTripStatistics(final Context context, final StatisticListener listener, String tripUUID) {
        BumpyService bumpyService = BumpyServiceBuilder.createService(BumpyService.class);
        bumpyService.getTripByTripUUID(tripUUID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<PastTripDetailedResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        //Do nothing
                    }

                    @Override
                    public void onSuccess(Response<PastTripDetailedResponse> response) {
                        Log.d("Get trip statistics", "Get trip statistics response: " + response.message());
                        if (!response.isSuccessful()) {
                            Toast.makeText(context, R.string.get_trip_stats_not_successful, Toast.LENGTH_SHORT).show();
                            listener.onError();
                        } else {
                            listener.onStatisticDone(response.body());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("Get trip statistics", "Failed to retrieve trip statistics: "  + e.getMessage());
                        Toast.makeText(context, R.string.get_trip_stats_not_successful, Toast.LENGTH_SHORT).show();
                        listener.onError();
                    }
                });
    }
}
