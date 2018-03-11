package com.furahitechstudio.furahitechpay.listeners;

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
 * <h1>PartialLogListener</h1>
 * <p>
 *     PartialLogListener is responsible for listening partial payment logging into a file on server side
 * </p>
 *
 * @author Lukundo Kileha (kileha3)
 *         lkileha@furahitech.co.tz
 *
 */
public interface PartialLogListener {
    /**
     * Invoke when partial paymment details are logged into a file on the server
     * @param isLogged TRUE when logged, FALSE otherwise
     */
    void onPartialPaymentLogged(boolean isLogged);
}
