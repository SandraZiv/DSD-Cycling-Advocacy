package com.cycling_advocacy.bumpy.net;

import android.content.Context;
import android.widget.Toast;

import com.cycling_advocacy.bumpy.R;
import com.cycling_advocacy.bumpy.achievements.util.AchievementsPrefs;
import com.cycling_advocacy.bumpy.net.model.ApiResponse;
import com.cycling_advocacy.bumpy.net.service.BumpyService;
import com.cycling_advocacy.bumpy.net.service.BumpyServiceBuilder;

import java.util.Date;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class DataManager {

    public static void deleteTrip(Context context, String tripUUID, Date tripStartTime) {
        BumpyService bumpyService = BumpyServiceBuilder.createService(BumpyService.class);
        bumpyService.deleteTrip(tripUUID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<ApiResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<ApiResponse> apiResponseResponse) {
                        Toast.makeText(context, R.string.deleted_successfully, Toast.LENGTH_SHORT).show();
                        AchievementsPrefs.decreaseDailyTripCount(context, tripStartTime.getTime());
                        AchievementsPrefs.decreaseTotalTripCount(context);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
}
