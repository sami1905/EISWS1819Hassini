package com.example.sami.diabetec;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi {

    @GET("events")
    Call<List<Event>> getEvents();

    @POST("events")
    Call<Event>createPost(@Body Event event);
}
