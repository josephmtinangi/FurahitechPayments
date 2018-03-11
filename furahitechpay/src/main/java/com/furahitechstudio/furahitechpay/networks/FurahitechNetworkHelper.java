package com.furahitechstudio.furahitechpay.networks;


import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.furahitechstudio.furahitechpay.utils.Furahitech.GATEWAY_WAZOHUB_ENDPOINT;



/*
 * Copyright (c) 2018 Lukundo Kileha
 *
 * Licensed under The MIT License,
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://opensource.org/licenses/MIT
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 */


/**
 * <h1>FurahitechNetworkHelper</h1>
 * <p>
 *     FurahitechNetworkHelper is a class which builds the request stack to support on all network operations
 * </p>
 *
 * @author Lukundo Kileha (kileha3)
 *         lkileha@furahitech.co.tz
 */
class FurahitechNetworkHelper {
    /**
     * Get API client with authorization
     * @param endPoint: Your server's URL
     * @param authorization: Auth parameter
     * @return Retrofit object
     */
    Retrofit getApiClient(@Nullable String endPoint, final String authorization) {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request newRequest;
                        if(authorization!=null){
                            newRequest  = chain.request().newBuilder()
                                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                    .addHeader("Authorization", "Bearer " + authorization)
                                    .build();
                        }else{
                            newRequest  = chain.request().newBuilder()
                                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                    .build();
                        }
                        return chain.proceed(newRequest);
                    }
                })
                .build();

        return new Retrofit.Builder()
                .baseUrl(endPoint==null ? GATEWAY_WAZOHUB_ENDPOINT:endPoint)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    /**
     * Get API client with base URL as a parameter: For different base URL calls
     * @param endPoint: Your server's URL
     * @return Retrofit object
     */
    Retrofit getApiClient(String endPoint) {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(endPoint)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
