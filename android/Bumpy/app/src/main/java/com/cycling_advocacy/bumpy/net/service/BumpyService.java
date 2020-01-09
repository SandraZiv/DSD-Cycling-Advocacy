package com.cycling_advocacy.bumpy.net.service;

import com.cycling_advocacy.bumpy.entities.Trip;
import com.cycling_advocacy.bumpy.net.model.ApiResponse;
import com.cycling_advocacy.bumpy.net.model.PastTripDetailedResponse;
import com.cycling_advocacy.bumpy.net.model.PastTripGeneralResponse;

import java.util.List;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface BumpyService {

    @POST("trip/insertNewTrip")
    Single<Response<ApiResponse>> insertNewTrip(@Body Trip trip);

    @Multipart
    @POST("trip/uploadMotionFile")
    Single<Response<ApiResponse>> uploadMotionData(@Part("tripUUID") String tripUUID, @Part MultipartBody.Part file);

    @GET("trip/getTripsByDeviceUUID")
    Single<Response<List<PastTripGeneralResponse>>> getTripsByDeviceUUID(@Query("deviceUUID") String deviceUUID);

    @GET("trip/getTripByTripUUID")
    Single<Response<PastTripDetailedResponse>> getTripByTripUUID(@Query("tripUUID") String tripUUID);

    @DELETE("trip/deleteTrip")
    Single<Response<ApiResponse>> deleteTrip(@Query("tripUUID") String tripUUID);

    @GET("device/getShortDeviceUUID")
    Single<Response<String>> getShortDeviceUUID(@Query("deviceUUID") String deviceUUID);
}
