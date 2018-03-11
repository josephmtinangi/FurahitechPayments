package com.furahitechstudio.furahitechpay.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.furahitechstudio.furahitechpay.FurahitechPay;
import com.furahitechstudio.furahitechpay.R;
import com.furahitechstudio.furahitechpay.models.ModelWazoHub;
import com.furahitechstudio.furahitechpay.models.PaymentRequest;

import java.util.HashMap;
import java.util.Random;

import static com.furahitechstudio.furahitechpay.utils.Furahitech.ALLOWED_CHARACTERS;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.LIB_TAG;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentConstant.GATEWAY_MPESA;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentConstant.GATEWAY_NONE;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentConstant.GATEWAY_STRIPE;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentConstant.GATEWAY_TIGOPESA;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentConstant.STATUS_CANCELLED;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentDataKeys.COUNTRY_CODE;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentDataKeys.COUNTRY_NAME;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentDataKeys.CUSTOMER_EMAIL;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentDataKeys.CUSTOMER_FNAME;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentDataKeys.CUSTOMER_LNAME;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentDataKeys.CUSTOMER_PHONE;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentDataKeys.DESCRIPTION;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentDataKeys.MERCHANT_ACCOUNT;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentDataKeys.MERCHANT_KEY;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentDataKeys.MERCHANT_MSIDN;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentDataKeys.MERCHANT_PIN;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentDataKeys.MERCHANT_SECRET;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentDataKeys.OPERATION_ENV;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentDataKeys.OPERATOR;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentDataKeys.TRANS_AMOUNT;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentDataKeys.TRANS_CURRENCY;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentDataKeys.TRANS_FEE;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentDataKeys.TRANS_LANGUAGE;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentDataKeys.TRANS_MSID;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentDataKeys.TRANS_REFID;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentDataKeys.TRANS_REQID;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentDataKeys.TRANS_STATUS_CODE;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentDataKeys.TRANS_TAX;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentDataKeys.TRANS_TOKEN;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentDataKeys.TRANS_UUID;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentDataKeys.WAZO_CLIENT_ID;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentDataKeys.WAZO_CLIENT_SECRET;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentDataKeys.WAZO_GRANT_TYPE;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentDataKeys.WAZO_SCOPE;

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
 * <h1>FurahitechUtils</h1>
 * <p>
 *     FurahitechUtils is helper class responsible for handling small operation like conversions, parsing and other util functions..
 * </p>
 *
 * @author Lukundo Kileha (kileha3)
 *         lkileha@furahitech.co.tz
 */

public class FurahitechUtils {

    /**
     * Function to log all operation events
     * @param isError TRUE if is error otherwise false
     * @param gateway as indicated on
     * @param message message to be logged
     */
    public static void logEvent(boolean isError,Enum gateway,String message){
        if(isError){
            Log.e(LIB_TAG,getGatewayName(gateway)+": "+message);
        }else{
            Log.d(LIB_TAG,getGatewayName(gateway)+": "+message);
        }
    }


    /**
     * Get gateway name for the logger
     * @param gateway: Gateway type tag
     * @return String: Gateway name
     */
    private static String getGatewayName(Enum gateway){
        return gateway==GATEWAY_MPESA ? "Vodacom"
                :(gateway==GATEWAY_STRIPE ? "Stripe"
                :(gateway==GATEWAY_TIGOPESA ? "Tigo"
                :"CoreAPI"));
    }

    /**
     * Show network dialog error
     * @param context: Application context
     * @param gateway: Gateway currently on operation
     */
    public static void showNetworkErrorDialog(final Context context,Enum gateway){
        Furahitech.currentRetryCount=0;
        logEvent(true,gateway,"You are currently not connected to the network");
        try{
            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setTitle("Connection problem");
            builder.setMessage("You are currently not connected to the network, make sure you are connected before doing anything");
            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Check if there is internet connection
     * @return boolean: TRUE if connected otherwise false
     */
    public static boolean isConnected(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null
                    && activeNetwork.isConnectedOrConnecting();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Formatting phone number for proper and standard use
     * @param phone customer phone number
     * @return String: Formatted phone number
     */
    public static String formatPhoneNumber(String phone){
      return "255"+phone;
    }

    /**
     * Responsible for deciding the right MNO to operate ON
     * @param phone Customer phone number
     * @return String : Payment gateway MNO
     */
    public static Furahitech.PaymentConstant getPaymentMNO(String phone){
        if(phone.startsWith("71") || phone.startsWith("65") || phone.startsWith("67")){
            return GATEWAY_TIGOPESA;
        }else if(phone.startsWith("74") || phone.startsWith("75") || phone.startsWith("76")){
            return GATEWAY_MPESA;
        }else{
            return GATEWAY_NONE;
        }

    }


    /**
     * generate unique ID for transaction reference
     * @param length character length
     * @param gateway gateway type as indicated on PaymentConstants
     * @return String : reference ID
     */
    public static String generateRefCode(@Nullable Integer length,Enum gateway){
        int refLength=length==null ? 12:length;
        final Random random=new Random();
        final StringBuilder builder=new StringBuilder(refLength);
        for(int i=0;i<refLength;++i){
            builder.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        }
        logEvent(false,gateway,"generated: refId="+builder.toString());
        return builder.toString();
    }

    /**
     * Responsible for shoeing snack messages
     * @param view: CoordinationLayout
     * @param message: Message to be shown
     * @param islong: True when is LENGTH_INDEFINITE else false
     */
    public static void showSnackMessage(View view, String message, boolean islong){
        Snackbar.make(view, message, islong ? Snackbar.LENGTH_INDEFINITE: Snackbar.LENGTH_LONG).show();
    }

    /**
     * Build data to send to stripe for the transaction auth
     * @param request PaymentRequest params
     * @return HashMap of request param
     */
    public static HashMap<String,String> getCardPayment(PaymentRequest request){
        HashMap<String,String> data=new HashMap<>();
        data.put(MERCHANT_KEY,request.getCardMerchantSecret());
        data.put(TRANS_AMOUNT,String.valueOf((request.getTransactionAmount()
                +request.getTransactionTax() +request.getTransactionFee())));
        data.put(DESCRIPTION,request.getPaymentDesc());
        data.put(TRANS_TOKEN,request.getTransactionID());
        data.put(TRANS_CURRENCY,request.getDefaultCurrency());
        data.put(CUSTOMER_EMAIL,request.getCustomerEmailAddress());
        logEvent(false,GATEWAY_STRIPE,"Converted: data="+data.toString());
        return data;
    }


    /**
     * Get wazohub auth parameter for token request
     * @param request PaymentRequest params
     * @return HashMap of request param
     */
    public static HashMap<String,String> getWazoAuthParam(PaymentRequest request, String wazoScope){
        HashMap<String,String> toneReParam=new HashMap<>();
        toneReParam.put(WAZO_GRANT_TYPE,"client_credentials");
        toneReParam.put(WAZO_CLIENT_ID,request.getWazoHubClientID());
        toneReParam.put(WAZO_SCOPE,wazoScope);
        toneReParam.put(WAZO_CLIENT_SECRET,request.getWazoHubClientSecret());
        return toneReParam;
    }

    /**
     * Responsible for constructing wazohub push parameters
     * @param gateWay: Gateway as indicated on constants
     * @param request PaymentRequest params
     * @return HashMap of request param
     */
    public static HashMap<String,String> getWazoPushParam(Enum gateWay,PaymentRequest request){
        HashMap<String,String> pushRequestParam=new HashMap<>();
        pushRequestParam.put(TRANS_MSID,request.getCustomerPhone());
        int totalAmount=request.getTransactionAmount()+request.getTransactionTax() +request.getTransactionFee();
        pushRequestParam.put(TRANS_AMOUNT,String.valueOf(totalAmount));
        pushRequestParam.put(OPERATOR,getGatewayName(gateWay).toLowerCase());
        pushRequestParam.put(TRANS_LANGUAGE,request.getDefaultLanguage());
        pushRequestParam.put(TRANS_REQID,request.getTransactionID());
        return pushRequestParam;
    }


    /**
     * Responsible for constructing tigopesa payment parameters
     * @param request PaymentRequest params
     * @return HashMap of transaction param
     */
    public static HashMap<String,String> getTigoPesaParam(PaymentRequest request){
        HashMap<String,String> data=new HashMap<>();
        data.put(MERCHANT_KEY,request.getTigoMerchantKey());
        data.put(MERCHANT_SECRET,request.getTigoMerchantSecret());
        data.put(MERCHANT_ACCOUNT,request.getTigoMerchantName());
        data.put(MERCHANT_MSIDN,request.getTigoMerchantNumber());
        data.put(MERCHANT_PIN,request.getTigoMerchantPin());
        data.put(TRANS_AMOUNT,String.valueOf((request.getTransactionAmount()
                +request.getTransactionTax()+request.getTransactionFee())));
        data.put(TRANS_TOKEN,request.getTransactionID());
        data.put(TRANS_CURRENCY,request.getDefaultCurrency());
        data.put(TRANS_TAX,String.valueOf(request.getTransactionTax()));
        data.put(TRANS_FEE,String.valueOf(request.getTransactionFee()));
        data.put(TRANS_LANGUAGE, request.getDefaultLanguage());
        data.put(OPERATION_ENV, FurahitechPay.getInstance().getPaymentEnvironment());
        data.put(COUNTRY_CODE, request.getDefaultCountryCode());
        data.put(COUNTRY_NAME, request.getDefaultCountryName());
        data.put(CUSTOMER_FNAME, request.getCustomerFirstName());
        data.put(CUSTOMER_LNAME, request.getCustomerLastName());
        data.put(CUSTOMER_EMAIL, request.getCustomerEmailAddress());
        data.put(CUSTOMER_PHONE,request.getCustomerPhone());
        logEvent(false,GATEWAY_TIGOPESA,"Converted: data="+data.toString());
        return data;
    }


    /**
     * Responsible for constructing partial data to be logged
     * @param gateWay: Gateway type
     * @param response TransactionResponse object
     * @return HashMap data
     */
    public static HashMap<String,String> getPartialLogData(Enum gateWay,ModelWazoHub.TransactionResponse response){
        PaymentRequest paymentRequest=FurahitechPay.getInstance().getPaymentRequest();
        HashMap<String,String> partialPayment=new HashMap<>();
        partialPayment.put(TRANS_UUID,response.getUid());
        partialPayment.put(TRANS_STATUS_CODE,response.getCode());
        partialPayment.put(TRANS_REFID,paymentRequest.getTransactionID());
        logEvent(false,gateWay,"Converted: data="+partialPayment.toString());
        return partialPayment;
    }


    /**
     * Responsible for formatting payment amount
     * @param price: Amount to be paid
     * @param currency: PaymentCurrency code (TZS, USD)
     * @return String: Formatted currency
     */
    @SuppressLint("DefaultLocale")
    public static String formatPrice(int price, @Nullable String currency){
        return String.format("%,d",Integer.parseInt(String.valueOf(price)))+"/="+(currency==null ? "":currency);
    }

    /**
     * Method to show dialog when use try to cancel payment getValue
     * @param activity application context
     */
    public static void showTaskCancelDialogOnSecure(final Activity activity){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.cancel_task_title));
        builder.setMessage(activity.getString(R.string.cancel_task_message));
        builder.setCancelable(false);
        builder.setNegativeButton(activity.getString(R.string.negative_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(activity.getString(R.string.positive_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FurahitechPay.getInstance().notifyStateChanged(STATUS_CANCELLED);
                activity.finish();
            }
        });
        builder.show();
    }
}