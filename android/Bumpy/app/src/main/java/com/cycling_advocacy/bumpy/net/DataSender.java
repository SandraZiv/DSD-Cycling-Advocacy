package com.cycling_advocacy.bumpy.net;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.cycling_advocacy.bumpy.entities.Trip;
import com.cycling_advocacy.bumpy.net.model.ApiResponse;
import com.cycling_advocacy.bumpy.net.service.BumpyService;
import com.cycling_advocacy.bumpy.net.service.BumpyServiceBuilder;
import com.cycling_advocacy.bumpy.utils.CsvMotionUtil;
import com.cycling_advocacy.bumpy.utils.GeneralUtil;

import java.io.File;
import java.util.Date;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class DataSender {

    // Minimum trip duration in seconds; trips shorter than this won't be stored/sent
    private static final int MIN_TRIP_DURATION = 300;

    // Minimum trip duration in kilometers; trips shorter than this won't be stored/sent
    private static final double MIN_TRIP_DISTANCE = 0.5;

    public static void sendData(Context context, Trip trip) {
        Date startTS = trip.getStartTs();
        Date stopTS = trip.getStopTs();
        long duration = GeneralUtil.getDurationInSeconds(startTS, stopTS);

        double distance = trip.getDistance();

        if (duration >= MIN_TRIP_DURATION && distance >= MIN_TRIP_DISTANCE) {
            sendLocationData(context, trip);
            sendMotionData(context, trip);
        } else {
            // The motion file is constructed during the trip so we need to delete it
            CsvMotionUtil.deleteMotionDataFile(context, trip.getTripUUID());
            Log.d("Trip end", "Trip duration or distance too short for the trip to be considered.");
            Log.d("Trip end", "Trip duration is " + duration + " while minimum is " + MIN_TRIP_DURATION);
            Log.d("Trip end", "Trip distance is " + distance + " while minimum is " + MIN_TRIP_DISTANCE);
            Toast.makeText(context, "Trip duration or distance too short for the trip to be considered", Toast.LENGTH_SHORT).show();
        }
    }

    private static void sendLocationData(final Context context, final Trip trip) {
        BumpyService bumpyService = BumpyServiceBuilder.createService(BumpyService.class);
        bumpyService.insertNewTrip(trip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<ApiResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        //Do nothing
                    }

                    @Override
                    public void onSuccess(Response<ApiResponse> response) {
                        Log.d("Location data", "Location data upload response for trip " + trip.getTripUUID() + ": " + response.message());
                        if (!response.isSuccessful()) {
                            Toast.makeText(context, "Location data upload not successful!: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("Location data", "Failed to upload location data for trip " + trip.getTripUUID() + " to the server: " + e.getMessage());
                    }
                });
    }

    private static void sendMotionData(final Context context, final Trip trip) {
        File motionDataFile = CsvMotionUtil.getMotionDataFile(context, trip.getTripUUID());

        //TODO: This should look like
        // 'RequestBody.create(MediaType.parse(getContentResolver().getType(Uri.fromFile(motionDataFile))), motionDataFile);'
        // but something with the URI doesn't work
        RequestBody motionDataFileRB = RequestBody.create(MediaType.parse("text/csv"), motionDataFile);
        MultipartBody.Part motionDataFilePart = MultipartBody.Part.createFormData("file", motionDataFile.getName(), motionDataFileRB);

        BumpyService bumpyService = BumpyServiceBuilder.createService(BumpyService.class);
        bumpyService.uploadMotionData(trip.getTripUUID(), motionDataFilePart)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<ApiResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        //Do nothing
                    }

                    @Override
                    public void onSuccess(Response<ApiResponse> response) {
                        Log.d("Motion data", "Motion data upload response for trip " + trip.getTripUUID() + ": " + response.message());
                        if (response.isSuccessful()) {
                            CsvMotionUtil.deleteMotionDataFile(context, trip.getTripUUID());
                        } else {
                            Toast.makeText(context, "Motion data upload not successful!: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("Motion data", "Failed to upload motion data for trip " + trip.getTripUUID() + " to the server: " + e.getMessage());
                    }
                });
    }
}
