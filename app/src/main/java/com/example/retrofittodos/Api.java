package com.example.retrofittodos;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    final static String ENDPOINT = "https://jsonplaceholder.typicode.com/";
    static Retrofit retrofit;

    public static Retrofit getClient(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}
