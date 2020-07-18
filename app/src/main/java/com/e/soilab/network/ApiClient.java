package com.e.soilab.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient
{
    public static final String BASE_URL = "https://soilabapi.com/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS) // connect timeout
                .writeTimeout(30, TimeUnit.SECONDS) // write timeout
                .readTimeout(30, TimeUnit.SECONDS)
                .build();


        Gson gson = new GsonBuilder().setLenient().create();

        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }
}

