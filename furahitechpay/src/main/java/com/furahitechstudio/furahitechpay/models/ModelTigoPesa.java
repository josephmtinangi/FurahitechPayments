package com.furahitechstudio.furahitechpay.models;

import com.google.gson.annotations.SerializedName;
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
 * <h1>ModelTigoPesa</h1>
 * <p>
 *     ModelTigoPesa is responsible for holding all Tigo pesa authentication request response
 * </p>
 *
 * @author Lukundo Kileha (kileha3)
 *         lkileha@furahitech.co.tz
 */
public class ModelTigoPesa {

    @SerializedName("transactionRefId")
    private String transactionRefId;

    @SerializedName("redirectUrl")
    private String redirectUrl;

    @SerializedName("authCode")
    private String authCode;

    @SerializedName("creationDateTime")
    private String creationDateTime;

    /**
     * Responible gor getting transaction reference ID
     * @return String: Reference ID
     */
    public String getTransactionRefId() {
        return transactionRefId;
    }

    public void setTransactionRefId(String transactionRefId) {
        this.transactionRefId = transactionRefId;
    }

    /**
     * Response for getting tigopesa secure page URL
     * @return String: URL
     */
    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    /**
     * Responsible for getting payment request authentication code
     * @return String: Auth code
     */
    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    /**
     * Responsible for getting request response creation time in milliseconds
     * @return String: Time
     */
    public String getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(String creationDateTime) {
        this.creationDateTime = creationDateTime;
    }
}
