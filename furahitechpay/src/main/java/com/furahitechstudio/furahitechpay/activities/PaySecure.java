package com.furahitechstudio.furahitechpay.activities;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.furahitechstudio.furahitechpay.FurahitechPay;
import com.furahitechstudio.furahitechpay.R;

import static com.furahitechstudio.furahitechpay.utils.Furahitech.PAYMENT_REDIRECTION_PARAM;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentConstant.GATEWAY_TIGOPESA;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentConstant.STATUS_FAILURE;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentConstant.STATUS_SUCCESS;
import static com.furahitechstudio.furahitechpay.utils.FurahitechUtils.logEvent;
import static com.furahitechstudio.furahitechpay.utils.FurahitechUtils.showTaskCancelDialogOnSecure;

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
 * <h1>PaySecure</h1>
 * <p>
 *     PaySecure handles tigo-pesa secure page operation, this is when user validation for the payment is made.
 * </p>
 *
 * @author Lukundo Kileha (kileha3)
 *         lkileha@furahitech.co.tz
 *
 */

public class PaySecure extends AppCompatActivity {

    private ProgressBar progressDialog;
    private WebView secureWebView;
    private boolean finished=false;
    private String redirectionURL;
    private ImageView emptyLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_secure);

        //Find and set views
        TextView toolBarTitle = findViewById(R.id.toolBarTitle);
        Toolbar toolbar = findViewById(R.id.toolBar);
        secureWebView = findViewById(R.id.tigoSecurePage);
        emptyLoading = findViewById(R.id.emptyLoading);
        progressDialog= findViewById(R.id.progressBar);
        setSupportActionBar(toolbar);

        toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));
        redirectionURL=getIntent().getStringExtra(PAYMENT_REDIRECTION_PARAM);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            secureWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            secureWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        secureWebView.clearCache(true);

        //Setting up webView
        secureWebView.clearHistory();
        secureWebView.getSettings().setJavaScriptEnabled(true);
        secureWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        secureWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        String title="Paying to "+ FurahitechPay.getInstance().getPaymentRequest().getTigoMerchantName();
        toolBarTitle.setText(title);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar !=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);

        }
        toolbar.setTitle("");

        this.progressDialog.setProgress(0);
        this.progressDialog.setMax(100);
        renderWebPage();
    }


    /**
     * Listens when activity starts
     */
    @Override
    protected void onStart() {
        super.onStart();
        logEvent(false, GATEWAY_TIGOPESA,"Landed to secure page");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && secureWebView.canGoBack()) {
            secureWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Responsible for handling activity destroying process
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        secureWebView.clearHistory();
        secureWebView.clearFormData();
    }

    /**
     * Listen when home navigation button is clicked or back button
     */
    @Override
    public void onBackPressed() {
        showTaskCancelDialogOnSecure(PaySecure.this);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            showTaskCancelDialogOnSecure(PaySecure.this);
        }
        return true;
    }

    /**
     * Interface to listen and getting page HTML content
     */
    private class MyJavaScriptInterface {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(String html) {
            //Strapping HTML page and extract keyword for the transaction status
            if(html.toLowerCase().contains(getString(R.string.success_swahili).toLowerCase())
                    || html.toLowerCase().contains(getString(R.string.success_english).toLowerCase())){

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!finished){
                            logEvent(false,GATEWAY_TIGOPESA,"Payment succeeded, exiting");
                            FurahitechPay.getInstance().notifyStateChanged(STATUS_SUCCESS);
                            finished=true;
                            finish();
                        }
                    }
                });

            }else if(html.toLowerCase().contains(getString(R.string.failure_swahili).toLowerCase())
                    || html.toLowerCase().contains(getString(R.string.failure_swahili_not_available).toLowerCase())
                    || html.toLowerCase().contains(getString(R.string.failure_blocked_eng).toLowerCase())
                    || html.toLowerCase().contains(getString(R.string.failure_blocked_swa).toLowerCase())
                    || html.toLowerCase().contains(getString(R.string.failure_swahili_no_account).toLowerCase())
                    || html.toLowerCase().contains(getString(R.string.failure_expired).toLowerCase())
                    || html.toLowerCase().contains(getString(R.string.failure_no_enough_funds).toLowerCase())
                    || html.toLowerCase().equals(getString(R.string.failure_networks).toLowerCase())
                    || html.toLowerCase().contains(getString(R.string.session_termination_message).toLowerCase())){

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!finished){
                            logEvent(false,GATEWAY_TIGOPESA,"Payment failed, exiting");
                            FurahitechPay.getInstance().notifyStateChanged(STATUS_FAILURE);
                            finished=true;
                            finish();
                        }
                    }
                });
            }

        }

    }

    /**
     * Method to render HTML web page and get web text
     */
    protected void renderWebPage(){
        secureWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
                progressDialog.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressDialog.setVisibility(View.GONE);
                secureWebView.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(!finished){
                    view.loadUrl(url);
                }
                return true;
            }
        });
        secureWebView.setWebChromeClient(new WebChromeClient(){
            public void onProgressChanged(WebView view, int newProgress){
                progressDialog.setProgress(newProgress);
                logEvent(false,GATEWAY_TIGOPESA,"Loading page Progress "+newProgress);
                if(newProgress == 100){
                    emptyLoading.setVisibility(View.GONE);
                    progressDialog.setVisibility(View.GONE);
                }
            }
        });
        secureWebView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        secureWebView.loadUrl(redirectionURL);
    }
}
