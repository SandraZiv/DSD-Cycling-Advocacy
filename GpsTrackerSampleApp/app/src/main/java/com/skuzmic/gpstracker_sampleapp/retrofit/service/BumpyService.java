package com.skuzmic.gpstracker_sampleapp.retrofit.service;

import com.skuzmic.gpstracker_sampleapp.entities.Response;
import com.skuzmic.gpstracker_sampleapp.entities.Trip;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

//TODO: This will be divided into TripService, MapService, etc. For now it is only a couple of API methods for testing and alpha-prototype purposes
//TODO: Note that at the time of writing the backend URL was HTTP, not HTTPS so 'android:usesCleartextTraffic="true"' was added to the AndroidManifest
public interface BumpyService {

    //This GET method was used in order to test out connection with the backend service and is not relevant for the alpha-prototype
    @GET("mapData/getRoadQualitySegments")
    Single<String> grqs(@Query("bottomLeftLat") int bllat, @Query("bottomLeftLon") int bllon, @Query("topRightLat") int trlat, @Query("topRightLon") int trlon);

    //TODO Should Single be used here?
    @POST("trip/insertNewTrip")
    Single<String> insertNewTrip(@Body Trip trip);
}
