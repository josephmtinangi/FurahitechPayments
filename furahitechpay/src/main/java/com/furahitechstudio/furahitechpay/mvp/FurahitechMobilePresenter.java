package com.furahitechstudio.furahitechpay.mvp;

import android.widget.ImageView;

import com.furahitechstudio.furahitechpay.models.PaymentStatus;

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
 * <h1>FurahitechMobilePresenter</h1>
 * <p>
 *     FurahitechMobilePresenter is class which is coordinated by mobile payment activity to present data to the views
 * </p>
 *
 * @author Lukundo Kileha (kileha3)
 *         lkileha@furahitech.co.tz
 *
 * @see FurahitechBasePresenter
 */
public interface FurahitechMobilePresenter extends FurahitechBasePresenter{

    /**
     * Setting up selected MNO details
     * @param mnoIcon MNO logo
     * @param phoneNumber Customer phone number
     * @param charCount character count during typing
     */
    void setMNO(ImageView mnoIcon, String phoneNumber, int charCount);

    /**
     * Initiate mobile payment process
     */
    void initializePayment();

    /**
     * Get currently selected MNO
     * @return String :MNO name
     */
     Enum getGateWayMNO();

    /**
     * check if payment MNO is supported
     * @param mnos MNO list
     * @param mnoName MNO  name
     * @return boolean, TRUE if supported, FALSE otherwise
     */
    boolean isSupported(Enum mnoName,Enum... mnos);

    /**
     * @see FurahitechBasePresenter
     */
    @Override
    void setIsExecuting(boolean isExecuting);

    /**
     * @see FurahitechBasePresenter
     */
    @Override
    boolean isExecuting();

    @Override
    void sendData(PaymentStatus status);

    /**
     * @see FurahitechBasePresenter
     */
    @Override
    void onDestroy();

    /**
     * @see FurahitechBasePresenter
     */
    @Override
    void goBack();

    /**
     * Checking partial payment status
     */
    void startStatusCheck();

    /**
     * Checking if push was received
     * @return TRUE if received, Otherwise false
     */
    boolean isPushReceived();

    /**
     * Setting push reception status
     * @param pushReceived TRUE if receied, FALSE otherwise
     */
    void setPushReceived(boolean pushReceived);

    /**
     * Getting currently selected payment gateway
     * @return Enum: Gateway
     */
    Enum getCurrentGateWay();

    /**
     * Setting up retry count counter
     * @param retryCount counter
     */
    void setCurrentRetryCount(int retryCount);

    String getSelectedGateway();
}
