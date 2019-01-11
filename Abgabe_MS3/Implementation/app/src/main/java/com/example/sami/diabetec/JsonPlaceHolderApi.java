package com.example.sami.diabetec;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {

    @GET("events")
    Call<List<Event>> getEvents();

    @POST("events")
    Call<Event>createEvent(@Body Event event);

    @POST("authorization")
    Call<Authorization>createAuthorization();

    @GET("dexcomValues")
    Call<List<DexcomValues>> getDexcomValues();


    @POST("dexcomValues/{date}")
    Call<DexcomValues>createDexcomValues(@Path("date") String date);
}
