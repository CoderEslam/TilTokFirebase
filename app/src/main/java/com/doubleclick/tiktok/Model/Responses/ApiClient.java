package com.doubleclick.tiktok.Model.Responses;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    //https://futurestud.io/tutorials/retrofit-getting-started-and-android-client


    public static final String BASE_URL = "http://www.rojkharido.com/tiktok/";

    public static Retrofit retrofit = null;

    public static Retrofit getApiClient(){
        if (retrofit==null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }

        return retrofit;
    }

}
