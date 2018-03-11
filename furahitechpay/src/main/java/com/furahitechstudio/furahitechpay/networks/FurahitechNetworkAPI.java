package com.furahitechstudio.furahitechpay.networks;

import com.furahitechstudio.furahitechpay.models.ModelLogs;
import com.furahitechstudio.furahitechpay.models.ModelPartial;
import com.furahitechstudio.furahitechpay.models.ModelStripe;
import com.furahitechstudio.furahitechpay.models.ModelTigoPesa;
import com.furahitechstudio.furahitechpay.models.ModelWazoHub;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

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
 * <h1>FurahitechNetworkAPI</h1>
 * <p>
 *     FurahitechNetworkAPI is an interface which defines all endpoints which will be exchanging data from.
 * </p>
 *
 * @author Lukundo Kileha (kileha3)
 *         lkileha@furahitech.co.tz
 */

public interface FurahitechNetworkAPI {
    /**
     * Request for authorization to send a transaction request from aggregator
     * @param requestParam: request param
     * @return AuthenticationResponse object
     */
    @FormUrlEncoded
    @POST("api/v1/access_token")
    Call<ModelWazoHub.AuthenticationResponse> requestAccessToken(@FieldMap HashMap<String, String> requestParam);

    /**
     * Request for a transaction from Mobile Network Operator
     * @param transactionDetails: request param
     * @return TransactionResponse object
     */
    @FormUrlEncoded
    @POST("api/v1/c2b/push/mpesa")
    Call<ModelWazoHub.TransactionResponse> payWithMpesa(@FieldMap HashMap<String, String> transactionDetails);

    /**
     * request for a transaction operation on your card
     * @param paymentDetails: request param
     * @return PaymentStripe object
     */
    @FormUrlEncoded
    @POST("v1/card")
    Call<ModelStripe> payWithCard(@FieldMap HashMap<String, String> paymentDetails);

    /**
     * Request for a transaction from Mobile Network Operator
     * @param paymentDetails request param
     * @return PaymentStripe object
     */

    @FormUrlEncoded
    @POST("v1/tigopesa")
    Call<ModelTigoPesa> payWithTigoPesa(@FieldMap HashMap<String, String> paymentDetails);

    /**
     * Request for logging partial payment to your server an wait for the callback
     * @param details partial payment params
     * @return ModelLogs object
     */
    @FormUrlEncoded
    @POST("v1/partial")
    Call<ModelLogs> logPartialPayment(@FieldMap HashMap<String, String> details);

    /**
     * Request for checking the status of the partial payment as logged before
     * @param uuid Payment UUID
     * @return ModelPartial object
     */
    @GET("v1/{uuid}/partial")
    Call<ModelPartial> checkPartialPayment(@Path("uuid") String uuid);
}
