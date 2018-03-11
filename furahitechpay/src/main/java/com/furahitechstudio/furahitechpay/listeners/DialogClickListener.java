package com.furahitechstudio.furahitechpay.listeners;

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
 * <h1>DialogClickListener</h1>
 * <p>
 *     DialogClickListener is a listener which listens for the button clicks on the payment completion dialog
 *     On both successful and failed transaction there will be a button to click which will redirect back to the app.
 * </p>
 *
 * @author Lukundo Kileha (kileha3)
 *         lkileha@furahitech.co.tz
 */


public interface DialogClickListener {
    /**
     * Invoked when close button is clicked from the status dialog
     * @param status Transaction status
     * @see PaymentStatus
     */
    void onClose(PaymentStatus status);
}
