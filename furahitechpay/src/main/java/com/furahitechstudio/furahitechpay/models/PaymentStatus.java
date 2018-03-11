package com.furahitechstudio.furahitechpay.models;

import java.io.Serializable;
import java.util.HashMap;

import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentConstant.STATUS_SUCCESS;


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
 * <h1>PaymentStatus</h1>
 * <p>
 *     PaymentStatus is responsible for holding all payment status details after payment task completion
 * </p>
 *
 * @author Lukundo Kileha (kileha3)
 *         lkileha@furahitech.co.tz

 */

public class PaymentStatus implements Serializable{
    private String paymentRefId;
    private String paymentCustomerId;
    private String paymentRiskLevel;
    private String paymentGateWay;
    private HashMap<String,String> paymentExtraParam;
    private int paymentAmount;
    private int paymentTimeStamp;
    private Enum paymentStatus;

    /**
     * Responsible for getting transaction getValue status
     * @return status
     */
    public Enum getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Enum paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    /**
     * Responsible for getting transaction unique identification number
     * @return String: Indetification number
     */
    public String getPaymentRefId() {
        return paymentRefId;
    }

    public void setPaymentRefId(String paymentRefId) {
        this.paymentRefId = paymentRefId;
    }

    /**
     * Responsible for getting amount deducted from customer's account
     * @return int: Amount
     */
    public int getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(int paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    /**
     * Responsible for getting customer identification
     * @return String: Email address, Phone number
     */
    public String getPaymentCustomerId() {
        return paymentCustomerId;
    }

    public void setPaymentCustomerId(String paymentCustomerId) {
        this.paymentCustomerId = paymentCustomerId;
    }

    /**
     * Responsible for getting trsanction timestamp
     * @return int: Timestamp
     */
    public int getPaymentTimeStamp() {
        return paymentTimeStamp;
    }

    public void setPaymentTimeStamp(int paymentTimeStamp) {
        this.paymentTimeStamp = paymentTimeStamp;
    }

    /**
     * Responsible for getting risk level associated with the transaction
     * @return String: level (Normal,High)
     */
    public String getPaymentRiskLevel() {
        return paymentRiskLevel;
    }

    public void setPaymentRiskLevel(String paymentRiskLevel) {
        this.paymentRiskLevel = paymentRiskLevel;
    }

    /**
     * Responsible for getting gateway on which the transaction was made
     * @return String: Gateway (tigo-pesa,m-pesa,visa,mastercard etc)
     */
    public String getPaymentGateWay() {
        return paymentGateWay;
    }

    public void setPaymentGateWay(String paymentGateWay) {
        this.paymentGateWay = paymentGateWay;
    }

    /**
     * Responsible for getting extra parameter passed on the status
     * @return HashMap: Extra data for the transaction
     */
    public HashMap<String, String> getPaymentExtraParam() {
        return paymentExtraParam;
    }

    public void setPaymentExtraParam(HashMap paymentExtraParam) {
        this.paymentExtraParam = paymentExtraParam;
    }

    public boolean isPaidSuccessfully(){
        return paymentStatus==STATUS_SUCCESS;
    }
}
