package com.cycling_advocacy.bumpy.net.service;

import com.cycling_advocacy.bumpy.entities.Trip;
import com.cycling_advocacy.bumpy.net.ApiResponse;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface BumpyService {

    @POST("trip/insertNewTrip")
    Single<Response<ApiResponse>> insertNewTrip(@Body Trip trip);

    @Multipart
    @POST("trip/uploadMotionFile")
    Single<Response<ApiResponse>> uploadMotionData(@Part("tripUUID") String tripUUID, @Part MultipartBody.Part file);
}
