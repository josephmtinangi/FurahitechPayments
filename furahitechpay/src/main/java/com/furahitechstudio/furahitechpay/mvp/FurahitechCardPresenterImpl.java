package com.furahitechstudio.furahitechpay.mvp;


import android.app.Activity;
import android.content.Intent;

import com.furahitechstudio.furahitechpay.models.PaymentStatus;

import static com.furahitechstudio.furahitechpay.utils.Furahitech.RESULT_PAYMENT_STATUS;
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
 * <h1>FurahitechCardPresenterImpl</h1>
 * <p>
 *     FurahitechCardPresenterImpl is an implementation of a mobile specific presenter in which holds operations logic
 * </p>
 *
 * @author Lukundo Kileha (kileha3)
 *         lkileha@furahitech.co.tz
 *
 * @see FurahitechCardPresenter
 */
public class FurahitechCardPresenterImpl implements FurahitechCardPresenter {
    private FurahitechCardView cardView;
    private boolean isExecuting=false;
    private Activity activity;

    /**
     * FurahitechCardPresenterImpl Constructor
     * @param cardView PaymentCardView object
     */
    public FurahitechCardPresenterImpl(FurahitechCardView cardView, Activity activity){
        this.cardView=cardView;
        this.activity=activity;
    }

    /**
     * @see FurahitechCardPresenter
     */
    @Override
    public void checkCardValidity() {
        if(cardView!=null){
            setIsExecuting(true);
            cardView.validateCard();
        }
    }

    /**
     * @see FurahitechCardPresenter
     */
    @Override
    public void initPayment() {
       cardView.payNow();
    }

    /**
     * @see FurahitechCardPresenter
     */
    @Override
    public void setIsExecuting(boolean isExecuting) {
        this.isExecuting=isExecuting;
    }

    /**
     * @see FurahitechCardPresenter
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
     * @see FurahitechCardPresenter
     */
    @Override
    public void onDestroy() {
        setIsExecuting(false);
        cardView=null;
    }

    /**
     * @see FurahitechCardPresenter
     */
    @Override
    public void goBack() {
        if(cardView!=null){
           if(isExecuting){
               cardView.showWarningDialog();
           }else{
               cardView.navigateToBack();
           }
        }
    }
}
