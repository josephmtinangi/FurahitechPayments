package com.furahitechstudio.furahitechpay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.furahitechstudio.furahitechpay.activities.PayCard;
import com.furahitechstudio.furahitechpay.activities.PayMobile;
import com.furahitechstudio.furahitechpay.listeners.CallBackListener;
import com.furahitechstudio.furahitechpay.listeners.StateChangedListener;
import com.furahitechstudio.furahitechpay.models.PaymentRequest;
import com.furahitechstudio.furahitechpay.utils.Furahitech;
import com.furahitechstudio.furahitechpay.utils.FurahitechException;
import com.furahitechstudio.furahitechpay.utils.FurahitechUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import static com.furahitechstudio.furahitechpay.networks.FurahitechNetworkCall.checkCallbackStatus;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.CALLBACK_CHECK_INTERVAL;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.CUSTOM_PHONE_NUMBER_HINT;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.CUSTOM_PHONE_NUMBER_MASK;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PAYMENT_REDIRECTION_PARAM;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.REQUEST_CODE_PAYMENT_STATUS;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentConstant.GATEWAY_MPESA;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentConstant.GATEWAY_NONE;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentConstant.GATEWAY_TIGOPESA;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentConstant.MODE_MOBILE;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentEnvironment.LIVE;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentEnvironment.SANDBOX;
import static com.furahitechstudio.furahitechpay.utils.FurahitechUtils.logEvent;

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
 * <h1>FurahitechPay</h1>
 * <p>
 *     FurahitechPay is singleton class which acts as a core for every operation within this API.
 * </p>
 *
 * @author Lukundo Kileha (kileha3)
 *         lkileha@furahitech.co.tz
 */
public class FurahitechPay{

    @SuppressLint("StaticFieldLeak")
    private static FurahitechPay furahitechPay;
    private Enum paymentMode;
    private String paymentEnvironment= LIVE;
    private PaymentRequest paymentRequest;
    private Activity activity;
    private Enum [] supportedGateway;
    private String customPhoneNumberHint=CUSTOM_PHONE_NUMBER_HINT;
    private String customPhoneNumberMask=CUSTOM_PHONE_NUMBER_MASK;
    private List<StateChangedListener> stateChangedListeners=new CopyOnWriteArrayList<>();

    private static Enum currentGateway;
    private static String transactionUUID;
    private static CallBackListener callBackListener;

    /**
     * Creating schedules for periodic callback status change
     */
    private Handler mHandler=new Handler();
    private Runnable mRunnable=new Runnable() {
        @Override
        public void run() {
            Furahitech.currentRetryCount++;
            checkCallbackStatus(activity,currentGateway,transactionUUID,callBackListener);
            mHandler.postDelayed(mRunnable, TimeUnit.SECONDS.toMillis(CALLBACK_CHECK_INTERVAL));
        }
    };



    /**
     * Responsible for getting PaymentGatewaysAPI instance as singleton
     * @return FurahitechPay object
     */
    public static FurahitechPay getInstance(){
        if(furahitechPay ==null){
            furahitechPay =new FurahitechPay();
            logEvent(false,GATEWAY_NONE,"API instance acquired");
        }
        return furahitechPay;
    }

    /**
     * Responsible for setting up application activity and statusChangeListeners
     * @param activity: Application activity
     * @return FurahitechPay :object
     */
    public FurahitechPay with(Activity activity){
        this.activity =activity;
        return this;
    }


    /**
     * Scheduling periodic callback status check from the server
     * @param gateway Currently selected Gateway
     * @param UUID Currently created UUID
     * @param listener Status check listener
     */
    public void startCallBackCheckTask(Enum gateway, String UUID, CallBackListener listener){
        currentGateway=gateway;
        transactionUUID=UUID;
        callBackListener=listener;
        mRunnable.run();
    }


    /**
     * Cancel periodic callback status check task
     */
    public void cancelCallBackCheckTask(){
        if(mHandler!=null && mRunnable!=null){
            mHandler.removeCallbacks(mRunnable);
        }
    }


    /**
     * Responsible for getting payment request object
     * @return PaymentRequest
     */
    public PaymentRequest getPaymentRequest(){
        return paymentRequest;
    }

    /**
     * Responsible for setting up payment mode (PAYMENT_MOBILE / PAYMENT_CARD)
     * @param paymentMode: Payment mode as indicated in PaymentMode
     * @return FurahitechPay: object
     */
    public FurahitechPay setPaymentMode(Enum paymentMode) {
        this.paymentMode = paymentMode;
        return this;
    }

    /**
     * Responsible for setting up payment environment
     * @param paymentEnvironment: Payment environment (LIVE/SANDBOX)
     * @return FurahitechPay: object
     */
    public FurahitechPay setPaymentEnvironment(String paymentEnvironment) {
        this.paymentEnvironment = paymentEnvironment;
        return this;
    }

    /**
     * Responsible for setting up payment request
     * @param request: Payment request object with all details
     *                      necessary for transaction initialization
     * @return FurahitechPay: Object
     */
    public FurahitechPay setPaymentRequest(PaymentRequest request) {
        this.paymentRequest = request;
        return this;
    }

    /**
     * Responsible for setting up list of supported Mobile Network Operators
     * @param supportedGateway List of supported Mobile Network Operators
     * @return FurahitechPay object
     */
    public FurahitechPay setSupportedGateway(Enum ... supportedGateway){
        this.supportedGateway =supportedGateway;
        return this;
    }

    public Enum[] getSupportedGateway() {
        return supportedGateway;
    }

    public String getPaymentEnvironment() {
        return paymentEnvironment;
    }

    public String getCustomPhoneNumberHint() {
        return customPhoneNumberHint;
    }

    /**
     * Responsible for setting up custom hint for customer phone number
     * @param customPhoneNumberHint custom hint
     * @return FurahitechPay object
     */
    public FurahitechPay setCustomPhoneNumberHint(String customPhoneNumberHint) {
        this.customPhoneNumberHint = customPhoneNumberHint;
        return this;
    }

    public String getCustomPhoneNumberMask() {
        return customPhoneNumberMask;
    }

    /**
     * Responsible for setting up custom mask for customer phone number
     * @param customPhoneNumberMask custom mask
     * @return FurahitechPay object
     */
    public FurahitechPay setCustomPhoneNumberMask(String customPhoneNumberMask) {
        this.customPhoneNumberMask = customPhoneNumberMask;
        return this;
    }

    public void registerStateListener(StateChangedListener listener){
        if(stateChangedListeners!=null){
            stateChangedListeners.add(listener);
        }
    }


    public void notifyStateChanged(Enum state){
        if(stateChangedListeners!=null){
            for(StateChangedListener listener:stateChangedListeners){
                listener.onTaskStateChanged(state);
            }
        }
    }

    public Enum getPaymentMode(){
        return paymentMode;
    }


    /**
     * Building logic to decide what logic to be executed
     */
    public void build(){
        if(activity !=null){
            if(paymentRequest!=null){
                if(paymentEnvironment!=null && ((paymentEnvironment.equals(SANDBOX) && paymentEnvironment.toLowerCase().contains(SANDBOX.toLowerCase()))
                        || (paymentEnvironment.equals(LIVE) && paymentEnvironment.toLowerCase().contains(LIVE.toLowerCase())))){

                    if(paymentMode !=null){
                        if(getPaymentRequest().getPaymentRequestEndPoint()!=null){
                            if(getPaymentRequest().getCustomerEmailAddress()!=null && getPaymentRequest().getCustomerFirstName()!=null
                                    && getPaymentRequest().getCustomerLastName()!=null){

                                if(getPaymentRequest().getTransactionAmount()>0){
                                    Intent resultIntent;
                                    if(paymentMode == MODE_MOBILE){
                                        if(supportedGateway !=null && supportedGateway.length>0){
                                            if(getPaymentRequest().getWazoHubClientID()!=null && getPaymentRequest().getWazoHubClientSecret()!=null){

                                                if((Arrays.asList(supportedGateway).indexOf(GATEWAY_MPESA)!=-1 && getPaymentRequest().getPaymentLogsEndPoint()!=null) || Arrays.asList(supportedGateway).indexOf(GATEWAY_TIGOPESA)!=-1){
                                                    resultIntent=new Intent(activity, PayMobile.class);
                                                    logEvent(false,GATEWAY_NONE,"Mobile payment selected");
                                                }else{
                                                    throw new FurahitechException("Missing payment logging endpoint",new Throwable("Provide log en-point"));
                                                }
                                            }else{
                                                throw new FurahitechException("Missing wazohub client details",new Throwable("Provide WazoHub client credentials"));
                                            }
                                        }else{
                                            throw new FurahitechException("Missing supported payment list",new Throwable("Select payment methods you support i.e MNO_TIGOPESA , MNO_MPESA"));
                                        }
                                    }else{
                                        if(getPaymentRequest().getCardMerchantKey()!=null && getPaymentRequest().getCardMerchantSecret()!=null){
                                            resultIntent=new Intent(activity, PayCard.class);
                                            logEvent(false,GATEWAY_NONE,"Card payment selected");
                                        }else{
                                            throw new FurahitechException("Card merchant details can't be null",new Throwable("Make sure you provide card merchant details"));
                                        }
                                    }
                                    resultIntent.putExtra(PAYMENT_REDIRECTION_PARAM,getPaymentRequest());
                                    activity.startActivityForResult(resultIntent, REQUEST_CODE_PAYMENT_STATUS);
                                }else{
                                    throw new FurahitechException("Total amount must be greater than zero",new Throwable("Make sure payable amount is greater than zero"));
                                }

                            }else{
                                throw new FurahitechException("Missing customer details",new Throwable("Provide customer and payment details"));
                            }
                        }else{
                            throw new FurahitechException("Missing HTTP payment base URL",new Throwable("Provide base URl for HTTP calls"));
                        }
                    }else{
                        throw new FurahitechException("Missing payment mode",new Throwable("Provide payment mode"));
                    }

                }else{
                    throw new FurahitechException("URL provided is not sandbox URL",new Throwable("Provide correct sandbox URL"));
                }

            }else{
                throw new FurahitechException("Missing payment request param",new Throwable("Provide payment request param"));
            }
        }else{
            throw new FurahitechException("Missing application activity and ",new Throwable("Provide application activity"));
        }

    }
}
