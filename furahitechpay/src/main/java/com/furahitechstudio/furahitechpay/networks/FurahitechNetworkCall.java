package com.furahitechstudio.furahitechpay.networks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.furahitechstudio.furahitechpay.FurahitechPay;
import com.furahitechstudio.furahitechpay.listeners.CallBackListener;
import com.furahitechstudio.furahitechpay.listeners.FurahitechBaseListener;
import com.furahitechstudio.furahitechpay.listeners.PartialLogListener;
import com.furahitechstudio.furahitechpay.listeners.PushMenuListener;
import com.furahitechstudio.furahitechpay.listeners.RedirectionListener;
import com.furahitechstudio.furahitechpay.listeners.TokenListener;
import com.furahitechstudio.furahitechpay.models.ModelLogs;
import com.furahitechstudio.furahitechpay.models.ModelPartial;
import com.furahitechstudio.furahitechpay.models.ModelStripe;
import com.furahitechstudio.furahitechpay.models.ModelTigoPesa;
import com.furahitechstudio.furahitechpay.models.ModelWazoHub;
import com.furahitechstudio.furahitechpay.models.PaymentRequest;
import com.furahitechstudio.furahitechpay.models.PaymentStatus;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.furahitechstudio.furahitechpay.utils.Furahitech.CALLBACK_CHECK_MAX_RETRY_COUNT;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.CallBackStatus.WAITING;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.GATEWAY_WAZOHUB_ENDPOINT;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentConstant.GATEWAY_STRIPE;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentConstant.GATEWAY_TIGOPESA;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentConstant.STATUS_FAILURE;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentConstant.STATUS_SUCCESS;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.currentRetryCount;
import static com.furahitechstudio.furahitechpay.utils.FurahitechUtils.getCardPayment;
import static com.furahitechstudio.furahitechpay.utils.FurahitechUtils.getPartialLogData;
import static com.furahitechstudio.furahitechpay.utils.FurahitechUtils.getTigoPesaParam;
import static com.furahitechstudio.furahitechpay.utils.FurahitechUtils.getWazoAuthParam;
import static com.furahitechstudio.furahitechpay.utils.FurahitechUtils.getWazoPushParam;
import static com.furahitechstudio.furahitechpay.utils.FurahitechUtils.isConnected;
import static com.furahitechstudio.furahitechpay.utils.FurahitechUtils.logEvent;
import static com.furahitechstudio.furahitechpay.utils.FurahitechUtils.showNetworkErrorDialog;

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
 * <h1>FurahitechNetworkCall</h1>
 * <p>
 *     FurahitechNetworkCall is a class which handles all network operations, initializes request, get response and parse those response
 * </p>
 *
 * @author Lukundo Kileha (kileha3)
 *         lkileha@furahitech.co.tz
 *
 * @see FurahitechNetworkAPI
 * @see FurahitechNetworkHelper
 */
public class FurahitechNetworkCall {
    private static FurahitechNetworkAPI furahitechNetworkAPI;

    /**
     * Method responsible for paying with card (Visa, Mastercard, American Express etc)
     * @param context Application context
     * @param furahitechBaseListener FurahitechBaseListener object
     */
    @SuppressLint("StaticFieldLeak")
    public static void payWithCard(final Context context,final FurahitechBaseListener furahitechBaseListener){
        if(isConnected(context)){
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    logEvent(false,GATEWAY_STRIPE,"Paying now...");
                    final PaymentRequest paymentRequest= FurahitechPay.getInstance().getPaymentRequest();
                    HashMap<String,String> param= getCardPayment(paymentRequest);
                    furahitechNetworkAPI =new FurahitechNetworkHelper().getApiClient(paymentRequest.getPaymentRequestEndPoint()).create(FurahitechNetworkAPI.class);
                    Call<ModelStripe> mResponse= furahitechNetworkAPI.payWithCard(param);

                    mResponse.enqueue(new Callback<ModelStripe>() {
                        @Override
                        public void onResponse(Call<ModelStripe> call, Response<ModelStripe> response) {
                            ModelStripe stripeResponse=response.body();
                            if(stripeResponse!=null){
                                logEvent(false,GATEWAY_STRIPE,"success: id="+stripeResponse.getId());
                                PaymentStatus status=new PaymentStatus();
                                String amount=String.valueOf(stripeResponse.getAmount());
                                status.setPaymentAmount(Integer.parseInt(amount.substring(0,amount.length()-2)));
                                status.setPaymentCustomerId(paymentRequest.getCustomerEmailAddress());
                                status.setPaymentRefId(stripeResponse.getId());
                                status.setPaymentStatus(stripeResponse.isPaid() ? STATUS_SUCCESS :  STATUS_FAILURE);
                                status.setPaymentTimeStamp(stripeResponse.getCreated());
                                status.setPaymentRiskLevel(stripeResponse.getRisk());
                                status.setPaymentGateWay(stripeResponse.getCard());
                                furahitechBaseListener.onPaymentCompleted(status);
                            }else{
                                furahitechBaseListener.onPaymentFailed();
                            }
                        }

                        @Override
                        public void onFailure(Call<ModelStripe> call, Throwable t) {
                            furahitechBaseListener.onPaymentFailed();
                            logEvent(true,GATEWAY_STRIPE,t.getMessage());
                        }
                    });
                    return null;
                }
            }.execute();
        }else{
            showNetworkErrorDialog(context,GATEWAY_STRIPE);
        }
    }



    @SuppressLint("StaticFieldLeak")
    public static void checkCallbackStatus(final Context context, final Enum gateway,final String uuid, final CallBackListener callBackListener){
        if(isConnected(context)){
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    logEvent(false,gateway,"ServerSide: count="+(currentRetryCount+1)+"/"+CALLBACK_CHECK_MAX_RETRY_COUNT);
                    furahitechNetworkAPI =new FurahitechNetworkHelper().getApiClient(FurahitechPay.getInstance().getPaymentRequest().getPaymentLogsEndPoint()).create(FurahitechNetworkAPI.class);
                    Call<ModelPartial> mResponse= furahitechNetworkAPI.checkPartialPayment(uuid);
                    mResponse.enqueue(new Callback<ModelPartial>() {
                        @Override
                        public void onResponse(Call<ModelPartial> call, Response<ModelPartial> response) {
                            ModelPartial  partialPayment=response.body();
                            logEvent(false,gateway,"checked: callback status ="+partialPayment.getCallback()+" at "
                                    +partialPayment.getCallbackTimestamp());
                            callBackListener.onReceived(true,partialPayment);
                        }

                        @Override
                        public void onFailure(Call<ModelPartial> call, Throwable t) {
                            logEvent(true,gateway,t.getLocalizedMessage());
                            ModelPartial partialCheck=new ModelPartial();
                            partialCheck.setCallback(WAITING);
                            partialCheck.setRefuid(uuid);
                            callBackListener.onReceived(false,partialCheck);
                        }
                    });
                    return null;
                }
            }.execute();
        }else{
            showNetworkErrorDialog(context,gateway);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public static void payWithTigoPesa(final Context context,final RedirectionListener redirectionListener){
        if(isConnected(context)){
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    logEvent(false,GATEWAY_TIGOPESA,"Requesting secure redirection URL from TigoPesa");
                    PaymentRequest paymentRequest=FurahitechPay.getInstance().getPaymentRequest();
                    HashMap<String,String> param= getTigoPesaParam(paymentRequest);
                    furahitechNetworkAPI =new FurahitechNetworkHelper().getApiClient(paymentRequest.getPaymentRequestEndPoint()).create(FurahitechNetworkAPI.class);
                    Call<ModelTigoPesa> mResponse= furahitechNetworkAPI.payWithTigoPesa(param);
                    mResponse.enqueue(new Callback<ModelTigoPesa>() {
                        @Override
                        public void onResponse(Call<ModelTigoPesa> call, Response<ModelTigoPesa> response) {
                            try{
                                ModelTigoPesa tigopesa=response.body();
                                logEvent(false,GATEWAY_TIGOPESA,"granted: redirectionURL="+tigopesa.getRedirectUrl()+" , created: "+tigopesa.getCreationDateTime());
                                redirectionListener.onRedirection(tigopesa);
                            }catch (Exception e){
                                logEvent(false,GATEWAY_TIGOPESA,"failed");
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ModelTigoPesa> call, Throwable t) {
                            logEvent(true,GATEWAY_TIGOPESA,t.getMessage());
                        }
                    });
                    return null;
                }
            }.execute();
        }else{
            showNetworkErrorDialog(context,GATEWAY_TIGOPESA);
        }
    }



    /**
     * Responsible for initializing push message from MNO (Vodacom)
     * @param context: Application Context
     * @param gateWay:  Gateway type (GATEWAY_TYPE_MPESA)
     * @param authToken: Authentication token
     * @param pushListener PaymentPushListener listener
     */
    @SuppressLint("StaticFieldLeak")
    public static void payWithWazoHub(final Context context, final Enum gateWay, final String authToken, final PushMenuListener pushListener){
        if(isConnected(context)){
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    logEvent(false,gateWay,"initialize payment to WazoHub");
                    PaymentRequest paymentRequest=FurahitechPay.getInstance().getPaymentRequest();
                    HashMap<String,String> param= getWazoPushParam(gateWay,paymentRequest);
                    furahitechNetworkAPI =new FurahitechNetworkHelper().getApiClient(null  ,authToken).create(FurahitechNetworkAPI.class);
                    Call<ModelWazoHub.TransactionResponse> mResponse= furahitechNetworkAPI.payWithMpesa(param);
                    mResponse.enqueue(new Callback<ModelWazoHub.TransactionResponse>() {
                        @Override
                        public void onResponse(Call<ModelWazoHub.TransactionResponse> call, Response<ModelWazoHub.TransactionResponse> response) {
                            ModelWazoHub.TransactionResponse transResponse=response.body();
                            logEvent(false,gateWay,"initialized:  UID="+transResponse.getUid()+" , HTTP Code: "+transResponse.getCode());
                            pushListener.onPushInitiated(transResponse);
                        }

                        @Override
                        public void onFailure(Call<ModelWazoHub.TransactionResponse> call, Throwable t) {
                            logEvent(true,gateWay,t.getMessage());
                        }
                    });
                    return null;
                }
            }.execute();
        }else{
            showNetworkErrorDialog(context,gateWay);
        }
    }


    @SuppressLint("StaticFieldLeak")
    public static void logPartialPaymentForCallback(final Context context, final Enum gateway, final ModelWazoHub.TransactionResponse response, final PartialLogListener partialLogListener){
        if(isConnected(context)){
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {

                    logEvent(false,gateway,"Log partial payment to own server");
                    HashMap<String,String> param= getPartialLogData(gateway,response);
                    furahitechNetworkAPI =new FurahitechNetworkHelper().getApiClient(FurahitechPay.getInstance().getPaymentRequest().getPaymentLogsEndPoint()).create(FurahitechNetworkAPI.class);
                    Call<ModelLogs> mResponse= furahitechNetworkAPI.logPartialPayment(param);
                    mResponse.enqueue(new Callback<ModelLogs>() {
                        @Override
                        public void onResponse(Call<ModelLogs> call, Response<ModelLogs> response) {
                            logEvent(false,gateway,"logged: fileName="+response.body().getMessage()+".json");
                            partialLogListener.onPartialPaymentLogged(!response.body().isError());
                        }

                        @Override
                        public void onFailure(Call<ModelLogs> call, Throwable t) {
                            logEvent(true,gateway,t.getLocalizedMessage());
                        }
                    });
                    return null;
                }
            }.execute();
        }else{
            showNetworkErrorDialog(context,gateway);
        }
    }




    /**
     * Responsible for requesting authentication token from WazoHUB
     * @param context Application Context
     * @param gateWay: Gateway type (GATEWAY_TYPE_MPESA)
     * @param tokenListener PaymentTokenListener listener
     */
    @SuppressLint("StaticFieldLeak")
    public static void initiateWazoHubPayments(final Context context, final String wazoScope, final Enum gateWay, final TokenListener tokenListener){
        if(isConnected(context)){
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {

                    logEvent(false,gateWay,"Requesting auth token from WazoHub...");
                    PaymentRequest paymentRequest=FurahitechPay.getInstance().getPaymentRequest();
                    HashMap<String,String> param= getWazoAuthParam(paymentRequest,wazoScope);
                    furahitechNetworkAPI =new FurahitechNetworkHelper().getApiClient(GATEWAY_WAZOHUB_ENDPOINT).create(FurahitechNetworkAPI.class);
                    Call<ModelWazoHub.AuthenticationResponse> mResponse= furahitechNetworkAPI.requestAccessToken(param);
                    mResponse.enqueue(new Callback<ModelWazoHub.AuthenticationResponse>() {
                        @Override
                        public void onResponse(Call<ModelWazoHub.AuthenticationResponse> call, Response<ModelWazoHub.AuthenticationResponse> response) {
                            ModelWazoHub.AuthenticationResponse authResponse=response.body();
                            logEvent(false,gateWay,"Acquired: token="+authResponse.getAccess_token()+"  , Expires in: "+authResponse.getExpires_in());
                            tokenListener.onTokenReceived(authResponse);
                        }

                        @Override
                        public void onFailure(Call<ModelWazoHub.AuthenticationResponse> call, Throwable t) {
                            logEvent(true,gateWay,t.getMessage());
                            tokenListener.onFailed();
                        }
                    });
                    return null;
                }
            }.execute();
        }else{
            showNetworkErrorDialog(context,gateWay);
        }
    }

}
