package com.cycling_advocacy.bumpy.net;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.cycling_advocacy.bumpy.entities.PastTrip;
import com.cycling_advocacy.bumpy.net.model.PastTripGeneralResponse;
import com.cycling_advocacy.bumpy.net.service.BumpyService;
import com.cycling_advocacy.bumpy.net.service.BumpyServiceBuilder;
import com.cycling_advocacy.bumpy.ui.pastTrips.PastTripsViewModel;
import com.cycling_advocacy.bumpy.utils.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class DataRetriever {

    public static void updatePastTripsList(final Context context, final PastTripsViewModel pastTripsViewModel) {
        //final String deviceUUID = PreferenceUtil.getDeviceUUID(context);
        final String deviceUUID = "12345"; //TODO: REMOVE!!
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
                            Toast.makeText(context, "Get trips for device not successful!: " + response.message(), Toast.LENGTH_SHORT).show();
                        } else {
                            List<PastTripGeneralResponse> pastTripsGeneral = response.body();
                            List<PastTrip> pastTrips = new ArrayList<>();
                            for (PastTripGeneralResponse pastTripGeneral : pastTripsGeneral) {
                                pastTrips.add(new PastTrip(pastTripGeneral.getStartTS().toString(), pastTripGeneral.getEndTS().toString(), "0", true));
                            }

                            pastTripsViewModel.setPastTripsLiveData(pastTrips);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("Get trips for device", "Failed to retrieve trips for device: "  + e.getMessage());
                    }
                });
    }
}