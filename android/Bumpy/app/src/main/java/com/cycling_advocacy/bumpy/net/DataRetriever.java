package com.cycling_advocacy.bumpy.net;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.cycling_advocacy.bumpy.R;
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

    // TODO: This implementation relies solely on trips being obtained from the web; To support trips obrained from the db as well I think we can just obtain
    //  them from the db in onSuccess and add them to the pastTrips list
    public static void getPastTripsList(final Context context, final PastTripsViewModel pastTripsViewModel) {
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
                        } else {
                            List<PastTripGeneralResponse> pastTripsGeneral = response.body();
                            List<PastTrip> pastTrips = new ArrayList<>();
                            for (PastTripGeneralResponse pastTripGeneral : pastTripsGeneral) {
                                // isUploaded is true since these trips are retrieved from the server
                                pastTrips.add(new PastTrip(pastTripGeneral.getStartTS(), pastTripGeneral.getEndTS(), pastTripGeneral.getDistance(),true));
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