package com.cycling_advocacy.bumpy.net.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class BumpyServiceBuilder {

    private static final String BASE_URL = "http://161.53.67.132:5000/v1/";

    private static Retrofit retrofit = createRetrofit();

    private static Retrofit createRetrofit() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new GsonUTC())
                .create();

        Retrofit.Builder retrofitBuiler = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(new OkHttpClient.Builder().build());
        return retrofitBuiler.build();
    }

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
