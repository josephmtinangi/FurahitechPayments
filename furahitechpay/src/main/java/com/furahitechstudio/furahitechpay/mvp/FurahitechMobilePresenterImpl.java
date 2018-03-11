package com.furahitechstudio.furahitechpay.mvp;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;

import com.furahitechstudio.furahitechpay.FurahitechPay;
import com.furahitechstudio.furahitechpay.R;
import com.furahitechstudio.furahitechpay.activities.PaySecure;
import com.furahitechstudio.furahitechpay.listeners.CallBackListener;
import com.furahitechstudio.furahitechpay.listeners.PartialLogListener;
import com.furahitechstudio.furahitechpay.listeners.PushMenuListener;
import com.furahitechstudio.furahitechpay.listeners.RedirectionListener;
import com.furahitechstudio.furahitechpay.listeners.TokenListener;
import com.furahitechstudio.furahitechpay.models.ModelPartial;
import com.furahitechstudio.furahitechpay.models.ModelTigoPesa;
import com.furahitechstudio.furahitechpay.models.ModelWazoHub;
import com.furahitechstudio.furahitechpay.models.PaymentRequest;
import com.furahitechstudio.furahitechpay.models.PaymentStatus;
import com.furahitechstudio.furahitechpay.utils.Furahitech;

import java.util.Date;
import java.util.HashMap;

import static com.furahitechstudio.furahitechpay.networks.FurahitechNetworkCall.initiateWazoHubPayments;
import static com.furahitechstudio.furahitechpay.networks.FurahitechNetworkCall.logPartialPaymentForCallback;
import static com.furahitechstudio.furahitechpay.networks.FurahitechNetworkCall.payWithTigoPesa;
import static com.furahitechstudio.furahitechpay.networks.FurahitechNetworkCall.payWithWazoHub;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.CALLBACK_CHECK_MAX_RETRY_COUNT;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.CallBackStatus.RECEIVED;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.CallBackStatus.TIMEOUT;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.CallbackState.STATE_RECEIVED;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PAYMENT_REDIRECTION_PARAM;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentConstant.GATEWAY_MPESA;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentConstant.GATEWAY_NONE;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentConstant.GATEWAY_TIGOPESA;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentConstant.STATUS_FAILURE;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentConstant.STATUS_SUCCESS;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentConstant.STATUS_TIMEOUT;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.RESULT_PAYMENT_STATUS;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.WazoScopes.SCOPE_MPESA;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.currentRetryCount;
import static com.furahitechstudio.furahitechpay.utils.FurahitechUtils.formatPhoneNumber;
import static com.furahitechstudio.furahitechpay.utils.FurahitechUtils.getPaymentMNO;
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
 * <h1>FurahitechMobilePresenterImpl</h1>
 * <p>
 *     FurahitechMobilePresenterImpl is an implementation of a mobile specific presenter in which holds operations logic
 * </p>
 *
 * @author Lukundo Kileha (kileha3)
 *         lkileha@furahitech.co.tz
 *
 * @see RedirectionListener
 * @see FurahitechMobilePresenter
 * @see TokenListener
 */

public class FurahitechMobilePresenterImpl implements RedirectionListener,FurahitechMobilePresenter,TokenListener {

    private FurahitechMobileView mobileView;
    private HashMap<Furahitech.PaymentConstant,Integer> paymentMethods;
    private String phoneNumber,currentUID,selectedMNO;
    private Furahitech.PaymentConstant supportedGateway,currentGateWay;
    private boolean isExecuting=false, isPushReceived=false, wasReceivedAtFirst=false;
    private Enum [] supported;
    private Activity activity;

    /**
     * PaymentMobilePresenterImpl Constructor
     * @param activity: Application context
     * @param mobileView :PaymentMobileView
     * @param paymentMethods PaymentMethods arrays
     * @param supported: String arrays of the supported payments
     */
    public FurahitechMobilePresenterImpl(Activity activity, FurahitechMobileView mobileView, HashMap<Furahitech.PaymentConstant,Integer> paymentMethods, Enum [] supported){
        this.activity=activity;
        this.mobileView=mobileView;
        this.paymentMethods=paymentMethods;
        this.supported=supported;
    }

    /**
     * @see FurahitechMobilePresenter
     */
    @Override
    public void setMNO(ImageView mnoIcon, String phoneNumber, int charCount) {
        if(((charCount>=3 || charCount<=4))
                || (charCount>=6 || charCount<=7)
                || (charCount>=5 || charCount<=6)){
            this.supportedGateway= isSupported(getPaymentMNO(phoneNumber),supported) ? getPaymentMNO(phoneNumber):GATEWAY_NONE;
            this.phoneNumber=phoneNumber;
            logEvent(false,GATEWAY_NONE,phoneNumber);
            mnoIcon.setImageResource(paymentMethods.get(supportedGateway));
            selectedMNO=getGateWayMNO()==GATEWAY_MPESA ? "M-Pesa":(getGateWayMNO()==GATEWAY_TIGOPESA? "Tigo Pesa":"Uknown");
        }
    }

    /**
     * @see FurahitechMobilePresenter
     */
    @Override
    public boolean isSupported(Enum mnoName,Enum ...mnos){
        for(Enum name:mnos){
            if(mnoName == name){
                return true;
            }
        }
        return false;
    }

    /**
     * @see FurahitechMobilePresenter
     */

    @Override
    public void initializePayment() {
        setIsExecuting(true);
        if(mobileView!=null){
            if(phoneNumber!=null && !phoneNumber.isEmpty()){
                PaymentRequest request= FurahitechPay.getInstance().getPaymentRequest();
                request.setCustomerPhone(formatPhoneNumber(phoneNumber));
                switch (supportedGateway){
                    case GATEWAY_TIGOPESA:
                        this.currentGateWay=GATEWAY_TIGOPESA;
                        mobileView.showSnackView(activity.getString(R.string.authentication_message),true);
                        payWithTigoPesa(activity,this);
                        break;
                    case GATEWAY_MPESA:
                        mobileView.showSnackView(activity.getString(R.string.authentication_message),true);
                        this.currentGateWay=GATEWAY_MPESA;
                        initiateWazoHubPayments(activity, SCOPE_MPESA,currentGateWay ,this);
                        break;
                    case GATEWAY_NONE:
                        mobileView.showSnackView(activity.getString(R.string.unsupported_paymeny_method),false);
                        break;
                }
            }else{
                mobileView.showSnackView(activity.getString(R.string.empty_mobile_details),false);
            }
        }
    }

    /**
     * @see FurahitechMobilePresenter
     */
    @Override
    public boolean isPushReceived() {
        return isPushReceived;
    }

    /**
     * @see FurahitechMobilePresenter
     */
    @Override
    public void setPushReceived(boolean pushReceived) {
        if(mobileView!=null){
            if(!wasReceivedAtFirst){
                this.isPushReceived = pushReceived;
                this.wasReceivedAtFirst=true;
                logEvent(false,currentGateWay, "First time receiving push menu");
            }else{
                logEvent(false,currentGateWay, "Transaction in process on push Menu");
                this.isPushReceived=true;
            }
        }
    }

    /**
     * @see FurahitechMobilePresenter
     */
    @Override
    public Enum getCurrentGateWay() {
        return currentGateWay;
    }

    @Override
    public void setCurrentRetryCount(int retryCount) {
        currentRetryCount=retryCount;
    }

    @Override
    public String getSelectedGateway() {
        return selectedMNO;
    }

    /**
     * @see FurahitechMobilePresenter
     */
    @Override
    public Enum getGateWayMNO() {
        return supportedGateway;
    }

    /**
     * @see FurahitechMobilePresenter
     */
    @Override
    public void setIsExecuting(boolean isExecuting) {
        this.isExecuting=isExecuting;
    }

    /**
     * @see FurahitechMobilePresenter
     */
    @Override
    public boolean isExecuting() {
        return isExecuting;
    }

    @Override
    public void sendData(PaymentStatus status) {
        Intent resultIntent=new Intent();
        resultIntent.putExtra(RESULT_PAYMENT_STATUS,status);
        activity.setResult(Activity.RESULT_OK,resultIntent);
        activity.finish();
    }

    /**
     * @see FurahitechMobilePresenter
     */
    @Override
    public void onDestroy() {
        setIsExecuting(false);
        mobileView=null;
    }

    /**
     * @see FurahitechMobilePresenter
     */
    @Override
    public void goBack() {
        if(mobileView!=null){
           if(isExecuting){
               mobileView.showWarningDialog();
           }else{
               mobileView.navigateToBack();
           }
        }
    }

    /**
     * @see FurahitechMobilePresenter
     */
    @Override
    public void startStatusCheck() {
       if(mobileView!=null){
           final String message="Checking status from "+selectedMNO+", processing..."+((currentRetryCount)*25)+"%";
           mobileView.showSnackView(message,true);
           FurahitechPay.getInstance().startCallBackCheckTask(currentGateWay,currentUID,paymentCallBackListener);
       }
    }

    /**
     * @see FurahitechMobilePresenter
     */
    @Override
    public void onTokenReceived(final ModelWazoHub.AuthenticationResponse response) {
        if(mobileView!=null){
            mobileView.showSnackView("Connecting to "+selectedMNO+" now..",true);
            payWithWazoHub(activity,getCurrentGateWay(),response.getAccess_token(),paymentPushListener);
        }
    }

    /**
     * @see FurahitechMobilePresenter
     */
    @Override
    public void onFailed() {
        if(mobileView!=null){
            mobileView.showSnackView(activity.getString(R.string.token_acquisition_failed),true);
        }
    }


    /**
     * @see FurahitechMobilePresenter
     */
    @Override
    public void onRedirection(final ModelTigoPesa redirection) {
      if(mobileView!=null){
          try{
              mobileView.showSnackView("Connecting to "+selectedMNO+" now...",true);
              logEvent(false,getCurrentGateWay(),"Redirecting to secure page");
              Intent intent=new Intent(activity, PaySecure.class);
              intent.putExtra(PAYMENT_REDIRECTION_PARAM,redirection.getRedirectUrl());
              activity.startActivity(intent);
          }catch (Exception e){
              mobileView.showSnackView(activity.getString(R.string.something_went_wrong),true);
          }
      }
    }

    /**
     * Responsible for listening Push Menu from MPESA
     */
    private PushMenuListener paymentPushListener=new PushMenuListener() {

        @Override
        public void onPushInitiated(ModelWazoHub.TransactionResponse response) {
            if(mobileView!=null){
                String message="UUID="+response.getUid()+", refCode="+response.getCode();
                FurahitechMobilePresenterImpl.this.currentUID=response.getUid();
                logEvent(false,getCurrentGateWay(),message);
                mobileView.showSnackView("Complete transaction on "+FurahitechPay.getInstance().getPaymentRequest().getCustomerPhone()+" ... ",true);
                logPartialPaymentForCallback(activity,getCurrentGateWay(),response,paymentPartialLogListener);
            }
        }
    };

    /**
     * Responsible for listening logging partial payment details to the file
     */
    private PartialLogListener paymentPartialLogListener=new PartialLogListener() {
        @Override
        public void onPartialPaymentLogged(boolean isLogged) {
           if(mobileView!=null){
               final String message="Checking status from "+selectedMNO+", processing..."+((currentRetryCount)*25)+"%";
               logEvent(false,currentGateWay,"partial payment was logged in "+(isLogged? "success":"fail"));
               mobileView.showSnackView(message,true);
               startStatusCheck();
           }
        }
    };

    /**
     * Responsible for listening callback from M-PESA
     */
    private CallBackListener paymentCallBackListener=new CallBackListener() {

        @Override
        public void onReceived(boolean isSuccess, final ModelPartial partialCheck) {
            if(mobileView!=null){
                //Check if callback was received and proceed with transaction status reporting
                if(partialCheck.getCallback().toLowerCase().equals(STATE_RECEIVED.toLowerCase()) && isExecuting()){
                    FurahitechPay.getInstance().cancelCallBackCheckTask();
                    mobileView.showSnackView(activity.getString(R.string.payment_message_completed),false);
                    PaymentRequest request=FurahitechPay.getInstance().getPaymentRequest();
                    PaymentStatus status=new PaymentStatus();
                    String amount=String.valueOf(request.getTransactionAmount());
                    status.setPaymentAmount(Integer.parseInt(amount.substring(0,amount.length()-2)));
                    status.setPaymentCustomerId(request.getCustomerEmailAddress());
                    status.setPaymentRefId(partialCheck.getRefuid());
                    status.setPaymentStatus(partialCheck.getStatus().equals(RECEIVED) ? STATUS_SUCCESS :  (partialCheck.getStatus().equals(TIMEOUT) ? STATUS_TIMEOUT:STATUS_FAILURE));
                    status.setPaymentTimeStamp(Integer.parseInt(partialCheck.getCallbackTimestamp()));
                    status.setPaymentRiskLevel("normal");
                    status.setPaymentGateWay(selectedMNO);
                    logEvent(false,currentGateWay,"Payment with "+getGateWayMNO()+" completed: status "+status.isPaidSuccessfully());
                    mobileView.onPaymentCompleted(status);
                }else{
                    final String message="Checking status from "+selectedMNO+", processing..."+((currentRetryCount)*25)+"%";
                    if(currentRetryCount > CALLBACK_CHECK_MAX_RETRY_COUNT && isExecuting()){
                        FurahitechPay.getInstance().cancelCallBackCheckTask();
                        logEvent(false,currentGateWay,"Terminate process after time-out");
                        partialCheck.setCallbackTimestamp(String.valueOf(new Date().getTime()/1000));
                        partialCheck.setRefuid(currentUID);
                        partialCheck.setStatus(TIMEOUT);
                        partialCheck.setCallback(STATE_RECEIVED);
                        onReceived(true,partialCheck);
                    }else{
                        if(isExecuting()){
                            mobileView.showSnackView(message,true);
                        }
                        logEvent(false,currentGateWay,message);
                    }
                }
            }
        }
    };

}
