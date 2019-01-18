package com.example.sami.diabetec;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {
    String url = "http://192.168.0.10:3000/";

    //dexcom-Methods

    @POST("authorization")
    Call<Authorization>postAuthorization();

    @POST("dexcomValues/{date}")
    Call<DexcomValues>postDexcomValues(@Path("date") String date);

    @GET("dexcomValues")
    Call<List<DexcomValues>> getDexcomValues();


    //userValues-Methods

    @POST("userValues")
    Call<Value>postValues();

    @GET("userValues")
    Call<List<Value>> getValues();

    @GET("userValues/{date}")
    Call<List<Value>> getValuesFromDate(@Path("date") String date);



    //events-Methods

    @POST("events")
    Call<Event>postEvent(@Body Event event);

    @GET("events")
    Call<List<Event>> getEvents();

    @GET("events/{date}")
    Call<List<Event>> getEventFromDate(@Path("date") String date);



    //statics-Methods
    @GET("valuesInPercent/{date}")
    Call<List<ValuesInPercent>> getValuesInPercent(@Path("date") String date);

    @GET("averageValue/{date}")
    Call<List<Statics>> getStatics(@Path("date") String date);















}
