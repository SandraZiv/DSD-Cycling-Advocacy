package com.skuzmic.gpstracker_sampleapp.retrofit.service;

import com.skuzmic.gpstracker_sampleapp.entities.ApiResponse;
import com.skuzmic.gpstracker_sampleapp.entities.Trip;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

//TODO: This will be divided into TripService, MapService, etc. For now it is only a couple of API methods for testing and alpha-prototype purposes
//TODO: Note that at the time of writing the backend URL was HTTP, not HTTPS so 'android:usesCleartextTraffic="true"' was added to the AndroidManifest
public interface BumpyService {

    @POST("trip/insertNewTrip")
    Single<Response<ApiResponse>> insertNewTrip(@Body Trip trip);

    @Multipart
    @POST("trip/uploadMotionFile")
    Single<Response<ApiResponse>> uploadMotionData(@Part("tripUUID") String tripUUID, @Part MultipartBody.Part file);
}
