package com.furahitechstudio.furahitechpay.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.furahitechstudio.furahitechpay.FurahitechPay;
import com.furahitechstudio.furahitechpay.R;
import com.furahitechstudio.furahitechpay.fragments.FurahitechResponseDialog;
import com.furahitechstudio.furahitechpay.listeners.DialogClickListener;
import com.furahitechstudio.furahitechpay.listeners.FurahitechBaseListener;
import com.furahitechstudio.furahitechpay.models.PaymentRequest;
import com.furahitechstudio.furahitechpay.models.PaymentStatus;
import com.furahitechstudio.furahitechpay.mvp.FurahitechCardPresenter;
import com.furahitechstudio.furahitechpay.mvp.FurahitechCardPresenterImpl;
import com.furahitechstudio.furahitechpay.mvp.FurahitechCardView;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

import static com.furahitechstudio.furahitechpay.networks.FurahitechNetworkCall.payWithCard;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentConstant.GATEWAY_STRIPE;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentConstant.STATUS_CANCELLED;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.RESULT_PAYMENT_STATUS;
import static com.furahitechstudio.furahitechpay.utils.FurahitechUtils.formatPrice;
import static com.furahitechstudio.furahitechpay.utils.FurahitechUtils.generateRefCode;
import static com.furahitechstudio.furahitechpay.utils.FurahitechUtils.logEvent;
import static com.furahitechstudio.furahitechpay.utils.FurahitechUtils.showSnackMessage;

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
 * <h1>PaymentCards</h1>
 * <p>
 *     PaymentCards handles all card payment tasks, it is responsible to coordinate views, models ans presenter operations,
 * </p>
 *
 * @author Lukundo Kileha (kileha3)
 *         lkileha@furahitech.co.tz
 *
 * @see com.furahitechstudio.furahitechpay.mvp.FurahitechCardView
 * @see FurahitechBaseListener
 * @see com.furahitechstudio.furahitechpay.listeners.DialogClickListener
 *
 */


public class PayCard extends AppCompatActivity implements FurahitechCardView,DialogClickListener,FurahitechBaseListener {

    private Toolbar toolBar;
    private FurahitechPay furahitechPay;
    private CardInputWidget cardInputWidget;
    private CoordinatorLayout coordinator;
    private FurahitechCardPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_card);

        //Finding and setting up views
        TextView totalAmount = findViewById(R.id.totalAmount);
        TextView customerName = findViewById(R.id.customerName);
        TextView extraFee = findViewById(R.id.extraFee);
        TextView customerEmail = findViewById(R.id.customerEmail);
        TextView paymentDescription = findViewById(R.id.paymentDescription);
        AppBarLayout appBarLayout = findViewById(R.id.AppBarLayout);
        toolBar=findViewById(R.id.toolBar);
        coordinator=findViewById(R.id.coordinator);
        cardInputWidget=findViewById(R.id.card_input_widget);

        appBarLayout.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));
        Button processPayment = findViewById(R.id.processPayment);
        presenter=new FurahitechCardPresenterImpl(this,this);

        //getting payment request details from PaymentGatewayAPI
        furahitechPay = FurahitechPay.getInstance();
        furahitechPay.getPaymentRequest().setTransactionID(generateRefCode(null,GATEWAY_STRIPE));
        PaymentRequest request= furahitechPay.getPaymentRequest();
        String fullName=request.getCustomerFirstName()+" "+request.getCustomerLastName();
        customerName.setText(fullName);
        paymentDescription.setText(request.getPaymentDesc());
        customerEmail.setText(request.getCustomerEmailAddress());
        totalAmount.setText(formatPrice(request.getTransactionAmount(),null));
        String message="With "+formatPrice(request.getTransactionFee(),null)+" as processing fee";
        extraFee.setText(message);
        extraFee.setVisibility(request.getTransactionFee()!=-1 ? View.VISIBLE:View.GONE);

        //Setting toolbar
        setToolbar();

        //Start card payment process
        processPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.checkCardValidity();
            }
        });
    }

    /**
     * @see FurahitechCardView
     */
    @Override
    public void setToolbar() {
        setSupportActionBar(toolBar);
        if (toolBar != null) {
            ActionBar actionBar = getSupportActionBar();
            if(actionBar !=null){
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
            }
            toolBar.setTitle("");
        }
    }

    /**
     * @see FurahitechCardView
     */
    @Override
    public void validateCard() {
        Card cardToSave = cardInputWidget.getCard();
        if(cardToSave!=null && cardToSave.getCVC()!=null && cardToSave.getExpMonth()!=null && cardToSave.getLast4()!=null) {
            showSnackMessage(coordinator,getString(R.string.card_validation),true);
            Stripe stripe=new Stripe(this);
            stripe.setDefaultPublishableKey(furahitechPay.getPaymentRequest().getCardMerchantKey());
            stripe.createToken(cardToSave, new TokenCallback() {
                @Override
                public void onError(Exception error) {
                    logEvent(true,GATEWAY_STRIPE,error.getMessage());
                    showSnackMessage(coordinator,getString(R.string.card_validation_failed),false);
                }
                @Override
                public void onSuccess(Token token) {
                    furahitechPay.getPaymentRequest().setCustomerCard(token.getCard().getNumber());
                    furahitechPay.getPaymentRequest().setPaymentType(token.getCard().getBrand());
                    furahitechPay.getPaymentRequest().setTransactionID(token.getId());
                    logEvent(false,GATEWAY_STRIPE,"success token: "+String.valueOf(token.getId()));
                    showSnackMessage(coordinator,getString(R.string.card_validated),false);
                    presenter.initPayment();
                }
            });
        }else{
            logEvent(true,GATEWAY_STRIPE,"Missing card details");
            presenter.setIsExecuting(false);
            showSnackMessage(coordinator,getString(R.string.empty_card_details),false);
        }
    }

    /**
     * @see FurahitechCardView
     */
    @Override
    public void onBackPressed() {
        presenter.goBack();
    }

    /**
     * @see FurahitechCardView
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                presenter.goBack();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @see FurahitechCardView
     */
    @Override
    public void navigateToBack() {
        Intent resultIntent=new Intent();
        PaymentStatus status=new PaymentStatus();
        status.setPaymentStatus(STATUS_CANCELLED);
        resultIntent.putExtra(RESULT_PAYMENT_STATUS,status);
        setResult(Activity.RESULT_OK,resultIntent);
        finish();
    }

    /**
     * @see FurahitechCardView
     */
    @Override
    public void showWarningDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.cancel_task_title));
        builder.setMessage(getString(R.string.cancel_task_message));
        builder.setPositiveButton(getString(R.string.positive_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                presenter.setIsExecuting(false);
                presenter.goBack();
            }
        });
        builder.setNegativeButton(getString(R.string.negative_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    /**
     * @see FurahitechCardView
     */
    @Override
    public void payNow() {
        showSnackMessage(coordinator,getString(R.string.payment_message_processing),true);
        payWithCard(this,this);
    }

    /**
     * @see FurahitechCardView
     */

    @Override
    public void onPaymentCompleted(PaymentStatus status) {
        presenter.setIsExecuting(false);
        showSnackMessage(coordinator,getString(R.string.payment_message_completed),false);
        android.app.FragmentManager fm = getFragmentManager();
        new FurahitechResponseDialog().getInstance(status).show(fm, "fragment_alert");
    }

    /**
     * @see FurahitechCardView
     */

    @Override
    public void onPaymentFailed() {
        showSnackMessage(coordinator,getString(R.string.payment_message_failed),true);
    }

    /**
     * @see FurahitechCardView
     */
    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    /**
     * @see FurahitechCardView
     */
    @Override
    public void onClose(PaymentStatus status) {
        logEvent(false,GATEWAY_STRIPE,"Going back to app context with \n"+status.toString());
        presenter.sendData(status);
    }
}
