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
 * <h1>FurahitechBasePresenter</h1>
 * <p>
 *     FurahitechBasePresenter is class which is coordinated by activities to present base data to the views
 * </p>
 *
 * @author Lukundo Kileha (kileha3)
 *         lkileha@furahitech.co.tz
 */
public interface FurahitechBasePresenter {
    /**
     * Handle activity cycle destroy
     */
    void onDestroy();

    /**
     * Handle back event on button click
     */
    void goBack();

    /**
     * Setting payment process execution status
     * @param isExecuting boolean: TRUE when executing , FALSE otherwise
     */
    void setIsExecuting(boolean isExecuting);

    /**
     * getting paymemt process execution status
     * @return boolean
     */
    boolean isExecuting();

    void sendData(PaymentStatus status);
}
