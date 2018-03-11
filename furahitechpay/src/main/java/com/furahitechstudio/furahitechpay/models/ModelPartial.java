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
 * <h1>ModelPartial</h1>
 * <p>
 *     ModelPartial is responsible for holding response from callback status check on server side.
 * </p>
 *
 * @author Lukundo Kileha (kileha3)
 *         lkileha@furahitech.co.tz
 */
public class ModelPartial {

    @SerializedName("status")
    private String status;
    @SerializedName("refId")
    private String refid;
    @SerializedName("refUID")
    private String refuid;
    @SerializedName("logged_timestamp")
    private String loggedTimestamp;
    @SerializedName("callback")
    private String callback;
    @SerializedName("callback_timestamp")
    private String callbackTimestamp;

    /**
     * Responsible for getting HTTP status code of the callback
     * @return String : HTTP status code
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    /**
     * Responsible for getting transaction reference ID
     * @return String: Reference ID
     */String getRefid() {
        return refid;
    }

    public void setRefid(String refid) {
        this.refid = refid;
    }

    /**
     * Responsible for getting transaction reference UID
     * @return String: Reference UID
     */
    public String getRefuid() {
        return refuid;
    }

    public void setRefuid(String refuid) {
        this.refuid = refuid;
    }

    /**
     * Responsible for getting partial payment logging into a file time
     * @return String: Timestamp
     */
    public String getLoggedTimestamp() {
        return loggedTimestamp;
    }

    public void setLoggedTimestamp(String loggedTimestamp) {
        this.loggedTimestamp = loggedTimestamp;
    }

    /**
     * Responsible for getting callback status (received,waiting)
     * @return String: status
     */
    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    /**
     * Responsible for getting callback reception timestamp
     * @return String: in milliseconds
     */
    public String getCallbackTimestamp() {
        return callbackTimestamp;
    }

    public void setCallbackTimestamp(String callbackTimestamp) {
        this.callbackTimestamp = callbackTimestamp;
    }
}
