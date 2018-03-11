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
 * <h1>FurahitechMobileView</h1>
 * <p>
 *     FurahitechMobileView is class which is responsible for setting up view on mobile processing activity
 * </p>
 *
 * @author Lukundo Kileha (kileha3)
 *         lkileha@furahitech.co.tz
 * @see FurahitechBaseView
 */

public interface FurahitechMobileView extends FurahitechBaseView {
    /**
     * Showing snack messages
     * @param message: Message to show
     * @param isLong: TRUE if is to stay indefinitely, FALSE otherwise
     */
   void showSnackView(String message, boolean isLong);

    /**
     * Handling payment completion status
     * @param paymentStatus: Payment status
     * @see PaymentStatus
     */
   void onPaymentCompleted(PaymentStatus paymentStatus);

    /**
     * @see FurahitechBaseView
     */
    @Override
    void setToolbar();

    /**
     * @see FurahitechBaseView
     */
    @Override
    void navigateToBack();

    /**
     * @see  FurahitechBaseView
     */
    @Override
    void showWarningDialog();
}
