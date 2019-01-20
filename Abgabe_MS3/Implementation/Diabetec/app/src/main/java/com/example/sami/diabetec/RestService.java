package com.example.sami.diabetec;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestService {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://192.168.0.15:3000/";

    public static Retrofit getRestService(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).
                    addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
