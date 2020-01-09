package com.cycling_advocacy.bumpy.net;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.cycling_advocacy.bumpy.R;
import com.cycling_advocacy.bumpy.TripUploadType;
import com.cycling_advocacy.bumpy.pending_trips.PendingTrip;
import com.cycling_advocacy.bumpy.pending_trips.PendingTripsManager;
import com.cycling_advocacy.bumpy.pending_trips.PendingTripsViewModel;
import com.cycling_advocacy.bumpy.entities.Trip;
import com.cycling_advocacy.bumpy.net.model.ApiResponse;
import com.cycling_advocacy.bumpy.net.service.BumpyService;
import com.cycling_advocacy.bumpy.net.service.BumpyServiceBuilder;
import com.cycling_advocacy.bumpy.utils.CsvMotionUtil;
import com.cycling_advocacy.bumpy.utils.NetworkUtil;
import com.cycling_advocacy.bumpy.utils.PreferenceUtil;

import java.io.File;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class DataSender {

    public static void sendData(Context context, Fragment fragment, Trip trip) {
        TripUploadType uploadType = PreferenceUtil.getTripUploadType(context);
        if (uploadType == TripUploadType.MANUAL) {
            buildUploadDialog(context, fragment, trip).show();
            return;
        }

        if ((uploadType == TripUploadType.WIFI && NetworkUtil.isWifiAvailable(context))
                || (uploadType == TripUploadType.MOBILE_DATA && NetworkUtil.isWifiOrMobileDataAvailable(context))) {
            sendLocationData(context, trip);
            sendMotionData(context, trip);
        } else {
            savePendingTripToDB(fragment, trip);

            // check to display error message
            int message;
            if (uploadType == TripUploadType.WIFI && !NetworkUtil.isWifiAvailable(context)) {
                message = R.string.trip_not_uploaded_wifi;
            } else {
                message = R.string.trip_not_uploaded_network;
            }

            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    public static void sendPendingData(Context context, Fragment fragment, PendingTrip pendingTrip) {
        if (!NetworkUtil.isNetworkConnected(context)) {
            Toast.makeText(context, R.string.trip_not_uploaded_network, Toast.LENGTH_LONG).show();
            return;
        }

        Trip trip = PendingTripsManager.convertToTrip(pendingTrip);
        if (trip != null) {
            sendLocationData(context, trip);
            sendMotionData(context, trip);

            deletePendingTrip(fragment, pendingTrip);
        } else {
            Toast.makeText(context, R.string.trip_not_uploaded_trip, Toast.LENGTH_LONG).show();
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

    private static AlertDialog.Builder buildUploadDialog(Context context, Fragment fragment, Trip trip) {
        return new AlertDialog.Builder(context)
                .setTitle(R.string.dialog_upload_trip_title)
                .setMessage(R.string.dialog_upload_trip_message)
                .setPositiveButton(R.string.dialog_yes, (dialogInterface, i) -> {
                    if (NetworkUtil.isNetworkConnected(context)) {
                        sendLocationData(context, trip);
                        sendMotionData(context, trip);
                    } else {
                        Toast.makeText(context, R.string.trip_not_uploaded_network, Toast.LENGTH_LONG).show();
                        savePendingTripToDB(fragment, trip);
                    }
                })
                .setNegativeButton(R.string.dialog_no, (dialogInterface, i) -> {
                    savePendingTripToDB(fragment, trip);
                });
    }

    private static void savePendingTripToDB(Fragment fragment, Trip trip) {
        PendingTripsViewModel viewModel = ViewModelProviders
                .of(fragment).get(PendingTripsViewModel.class);

        viewModel.insert(trip);
    }

    private static void deletePendingTrip(Fragment fragment, PendingTrip pendingTrip){
        PendingTripsViewModel viewModel = ViewModelProviders
                .of(fragment).get(PendingTripsViewModel.class);

        viewModel.delete(pendingTrip);
    }
}
