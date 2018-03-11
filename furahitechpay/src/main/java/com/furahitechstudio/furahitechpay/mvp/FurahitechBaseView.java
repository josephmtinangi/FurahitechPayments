package com.furahitechstudio.furahitechpay.mvp;
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
 * <h1>FurahitechBaseView</h1>
 * <p>
 *     FurahitechBaseView is class which extends base view functionality that will be used for all activities
 * </p>
 *
 * @author Lukundo Kileha (kileha3)
 *         lkileha@furahitech.co.tz
 */

public interface FurahitechBaseView {
    /**
     * Setting up toolbar
     */
    void setToolbar();

    /**
     * Handle when user navigate back using back key or home back key
     */
    void navigateToBack();

    /**
     * Show warning dialog to the user
     */
    void showWarningDialog();
}
