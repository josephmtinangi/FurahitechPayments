package com.furahitechstudio.furahitechpay.mvp;

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
 * <h1>FurahitechCardPresenter</h1>
 * <p>
 *     FurahitechCardPresenter is class which is coordinated by card payment activity to present data to the views
 * </p>
 *
 * @author Lukundo Kileha (kileha3)
 *         lkileha@furahitech.co.tz
 *
 * @see FurahitechBasePresenter
 */
public interface FurahitechCardPresenter extends FurahitechBasePresenter {

    /**
     * Check credit card details validity
     */
    void checkCardValidity();

    /**
     * Initializing card payment process
     */
    void initPayment();

    /**
     * @see FurahitechBasePresenter
     * @param isExecuting boolean: TRUE when executing , FALSE otherwise
     */
    @Override
    void setIsExecuting(boolean isExecuting);

    /**
     *  @see FurahitechBasePresenter
     * @return boolean
     */
    @Override
    boolean isExecuting();

    @Override
    void sendData(PaymentStatus status);

    /**
     *  @see FurahitechBasePresenter
     */
    @Override
    void onDestroy();

    /**
     *  @see FurahitechBasePresenter
     */
    @Override
    void goBack();
}
