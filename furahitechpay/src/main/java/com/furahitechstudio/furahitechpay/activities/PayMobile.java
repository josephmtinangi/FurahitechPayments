package com.furahitechstudio.furahitechpay.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.furahitechstudio.furahitechpay.FurahitechPay;
import com.furahitechstudio.furahitechpay.R;
import com.furahitechstudio.furahitechpay.fragments.FurahitechResponseDialog;
import com.furahitechstudio.furahitechpay.listeners.DialogClickListener;
import com.furahitechstudio.furahitechpay.listeners.StateChangedListener;
import com.furahitechstudio.furahitechpay.models.PaymentRequest;
import com.furahitechstudio.furahitechpay.models.PaymentStatus;
import com.furahitechstudio.furahitechpay.mvp.FurahitechMobilePresenter;
import com.furahitechstudio.furahitechpay.mvp.FurahitechMobilePresenterImpl;
import com.furahitechstudio.furahitechpay.mvp.FurahitechMobileView;
import com.furahitechstudio.furahitechpay.utils.Furahitech;

import java.util.Date;
import java.util.HashMap;

import br.com.sapereaude.maskedEditText.MaskedEditText;

import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentConstant.GATEWAY_MPESA;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentConstant.GATEWAY_NONE;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentConstant.GATEWAY_TIGOPESA;
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
 * <h1>PayMobile</h1>
 * <p>
 *     PayMobile handles all mobile payment tasks, it is responsible to coordinate views, models ans presenter operations.
 * </p>
 *
 * @author Lukundo Kileha (kileha3)
 *         lkileha@furahitech.co.tz
 *
 * @see FurahitechMobileView
 * @see StateChangedListener
 * @see DialogClickListener
 *
 */
public class PayMobile extends AppCompatActivity implements FurahitechMobileView,DialogClickListener,StateChangedListener{
    private Toolbar toolBar;
    private CoordinatorLayout coordinator;
    private FurahitechMobilePresenter presenter;
    private PaymentRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_mobile);

        TextView totalAmount = findViewById(R.id.totalAmount);
        TextView customerName = findViewById(R.id.customerName);
        TextView extraFee = findViewById(R.id.extraFee);
        TextView customerEmail = findViewById(R.id.customerEmail);
        TextView paymentDescription = findViewById(R.id.paymentDescription);
        Button processPayment = findViewById(R.id.processPayment);
        AppBarLayout appBarLayout = findViewById(R.id.AppBarLayout);
        final MaskedEditText personalPhone = findViewById(R.id.personalPhone);
        final ImageView paymentMethodIcon=findViewById(R.id.paymentMethodIcon);
        toolBar=findViewById(R.id.toolBar);
        coordinator=findViewById(R.id.coordinator);
        ImageView tigopesaIcon = findViewById(R.id.tigoPesa);
        ImageView mpesaIcon = findViewById(R.id.mPesa);
        appBarLayout.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));

        //Filling allowed payment methods
        HashMap<Furahitech.PaymentConstant,Integer> paymentMethods=new HashMap<>();
        paymentMethods.put(GATEWAY_TIGOPESA,R.drawable.tigo_icon);
        paymentMethods.put(GATEWAY_MPESA,R.drawable.voda_icon);
        paymentMethods.put(GATEWAY_NONE,R.drawable.agenda);

       /* Setting up details*/
        personalPhone.setHint(FurahitechPay.getInstance().getCustomPhoneNumberHint());
        personalPhone.setMask(FurahitechPay.getInstance().getCustomPhoneNumberMask());

        //getting payment request details from PaymentGatewayAPI
        FurahitechPay furahitechPay = FurahitechPay.getInstance();
        presenter=new FurahitechMobilePresenterImpl(this,this,paymentMethods,furahitechPay.getSupportedGateway());
        furahitechPay.getPaymentRequest().setTransactionID(generateRefCode(null,GATEWAY_NONE));
        request= furahitechPay.getPaymentRequest();
        String fullName=request.getCustomerFirstName()+" "+request.getCustomerLastName();
        customerName.setText(fullName);
        paymentDescription.setText(request.getPaymentDesc());
        customerEmail.setText(request.getCustomerEmailAddress());
        totalAmount.setText(formatPrice(request.getTransactionAmount(),null));
        String message=request.getTransactionFee()> 0 ? "With "+formatPrice(request.getTransactionFee(),null)+" as processing fee.":"With no hidden charges.";
        extraFee.setText(message);
        extraFee.setVisibility(View.VISIBLE);

        //Listen on phone number typing to detect gateway
        personalPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                presenter.setMNO(paymentMethodIcon,personalPhone.getRawText(),count);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //On finish key pressed on the soft keyboard
        personalPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
                return false;
            }
        });

        //Start processing payment
        processPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.initializePayment();
            }
        });

        //Setting up toolbar
        setToolbar();

        //Set visibility of MNO icons
        tigopesaIcon.setVisibility(presenter.isSupported(GATEWAY_TIGOPESA,furahitechPay.getSupportedGateway()) ? View.VISIBLE:View.GONE);
        mpesaIcon.setVisibility(presenter.isSupported(GATEWAY_MPESA,furahitechPay.getSupportedGateway()) ? View.VISIBLE:View.GONE);
    }



    @Override
    public void showSnackView(String message, boolean isLong) {
        showSnackMessage(coordinator,message,isLong);
    }

    @Override
    public void onPaymentCompleted(PaymentStatus paymentStatus) {
        if(!isFinishing()){
            presenter.setIsExecuting(false);
            showSnackMessage(coordinator,getString(R.string.payment_message_completed),false);
            android.app.FragmentManager fm = getFragmentManager();
            new FurahitechResponseDialog().getInstance(paymentStatus).show(fm, "fragment_alert");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }


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

    @Override
    public void onBackPressed() {
        presenter.goBack();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                presenter.goBack();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void navigateToBack() {
        Intent resultIntent=new Intent();
        PaymentStatus status=new PaymentStatus();
        status.setPaymentStatus(STATUS_CANCELLED);
        resultIntent.putExtra(RESULT_PAYMENT_STATUS,status);
        setResult(Activity.RESULT_OK,resultIntent);
        finish();
    }

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

    @Override
    protected void onStart() {
        FurahitechPay.getInstance().registerStateListener(this);
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        FurahitechPay.getInstance().cancelCallBackCheckTask();
        super.onDestroy();
    }

    @Override
    public void onWindowFocusChanged(boolean isMainViewLostFocus) {
        super.onWindowFocusChanged(isMainViewLostFocus);
        if(presenter!=null && GATEWAY_MPESA.equals(presenter.getGateWayMNO()) && presenter.isExecuting()){
            if(isMainViewLostFocus && presenter.isPushReceived()){
                logEvent(false,presenter.getCurrentGateWay(), "Is time to check status: true");
                presenter.setCurrentRetryCount(0);
                presenter.startStatusCheck();
            }else{
                presenter.setPushReceived(true);
            }
        }

    }

    @Override
    public void onClose(PaymentStatus status) {
        logEvent(false,presenter.getCurrentGateWay(),"Going back to app context with \n"+status.toString());
        presenter.sendData(status);
    }

    @Override
    public void onTaskStateChanged(Enum taskState) {
        request=FurahitechPay.getInstance().getPaymentRequest();
        PaymentStatus status=new PaymentStatus();
        status.setPaymentRiskLevel("normal");
        status.setPaymentStatus(taskState);
        status.setPaymentRefId(request.getTransactionID());
        status.setPaymentCustomerId(request.getCustomerEmailAddress());
        int totalAmount=request.getTransactionAmount()+request.getTransactionTax() +request.getTransactionFee();
        status.setPaymentAmount(totalAmount);
        status.setPaymentGateWay(presenter.getSelectedGateway());
        status.setPaymentTimeStamp((int)new Date().getTime());
        onPaymentCompleted(status);
    }
}
