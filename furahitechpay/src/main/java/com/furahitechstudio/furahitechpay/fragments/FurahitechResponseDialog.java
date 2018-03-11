package com.furahitechstudio.furahitechpay.fragments;


import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.furahitechstudio.furahitechpay.FurahitechPay;
import com.furahitechstudio.furahitechpay.R;
import com.furahitechstudio.furahitechpay.listeners.DialogClickListener;
import com.furahitechstudio.furahitechpay.models.PaymentStatus;

import static com.furahitechstudio.furahitechpay.utils.Furahitech.PAYMENT_REDIRECTION_PARAM;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentConstant.STATUS_CANCELLED;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentConstant.STATUS_SUCCESS;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentConstant.STATUS_TIMEOUT;

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
 * <h1>FurahitechResponseDialog</h1>
 * <p>
 *     FurahitechResponseDialog is responsible for showing payment status upon payment task completion
 * </p>
 *
 * @author Lukundo Kileha (kileha3)
 *         lkileha@furahitech.co.tz
 *
 */
public class FurahitechResponseDialog extends DialogFragment{
    private DialogClickListener listener;

    /**
     * Instantiate FurahitechResponseDialog as singleton
     * @param status Payment status
     * @return FurahitechResponseDialog object
     * @see PaymentStatus
     */
    public FurahitechResponseDialog getInstance(PaymentStatus status) {
       FurahitechResponseDialog ratingDialog=new FurahitechResponseDialog();
       Bundle bundle=new Bundle();
       bundle.putSerializable(PAYMENT_REDIRECTION_PARAM,status);
       ratingDialog.setArguments(bundle);
       return ratingDialog;
    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_response_dialog, container, false);
        TextView paymentMemo = view.findViewById(R.id.paymentMemo);
        TextView paymentStatus = view.findViewById(R.id.paymentStatus);
        ImageView paymentStatusIcon = view.findViewById(R.id.paymentStatusIcon);
        Button closeDialog = view.findViewById(R.id.closeDialog);
        FrameLayout paymentStatusHolder = view.findViewById(R.id.paymentStatusHolder);

        //Setting up payment status views from status itself
        final PaymentStatus status= (PaymentStatus) getArguments().getSerializable(PAYMENT_REDIRECTION_PARAM);

        if(status!=null){
            boolean isPaidSuccessfully= status.getPaymentStatus() == STATUS_SUCCESS;
            int paymentStatusColors= ContextCompat.getColor(getActivity(), isPaidSuccessfully ? R.color.colorSuccess
                    : status.getPaymentStatus() == STATUS_TIMEOUT ? android.R.color.black:
                    R.color.colorError);
            status.setPaymentExtraParam(FurahitechPay.getInstance().getPaymentRequest().getPaymentExtraParam());
            int paymentStatusIconsRes= isPaidSuccessfully ? R.drawable.ic_check_white_24dp: status.getPaymentStatus() == STATUS_TIMEOUT
                    ? R.drawable.ic_timer_off_white_24dp:R.drawable.ic_close_white_24dp;
            String paymentStatusLabel= isPaidSuccessfully ? "Success": status.getPaymentStatus() == STATUS_TIMEOUT ? "Timeout":"Failure";
            String message= status.getPaymentStatus() == STATUS_CANCELLED ? "We are sorry, it seems you have cancelled your payment task.Try next time."
                    : isPaidSuccessfully ? "Thank you for using our services, we have successfully received your payment."
                    : status.getPaymentStatus() == STATUS_TIMEOUT ? "Sorry, your payment process was timed out. If it was a success please contact us."
                    :"We are sorry, we didn't receive your payment.Please try again later.";

            //Setting data to the views and holders
            paymentMemo.setText(message);
            paymentStatus.setText(paymentStatusLabel);
            paymentStatusIcon.setImageResource(paymentStatusIconsRes);
            GradientDrawable bgShape = (GradientDrawable)paymentStatusHolder.getBackground();
            bgShape.setColor(paymentStatusColors);
            closeDialog.setBackgroundColor(paymentStatusColors);
            setCancelable(false);
            closeDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                    listener.onClose(status);
                }
            });
        }else{
            dismiss();
        }

        return view;
    }

    /**
     * Attach listener when dialog is attached to the activity
     * @param context Application context
     */
    @Override
    public void onAttach(Context context) {
        listener= (DialogClickListener) getActivity();
        super.onAttach(context);
    }

    /**
     * Setting up extra dialog views details on resiming it
     */
    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        super.onResume();
    }

}
