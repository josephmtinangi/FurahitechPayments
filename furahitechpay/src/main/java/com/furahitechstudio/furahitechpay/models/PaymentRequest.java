package com.furahitechstudio.furahitechpay.models;

import android.os.Bundle;


import java.io.Serializable;
import java.util.HashMap;

import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentCurrency.TSH;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentLanguage.SWAHILI;

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
 * <h1>PaymentRequest</h1>
 * <p>
 *     PaymentRequest is responsible for holding all payment request parameters which include Merchant details,
 *     Client details and end-points details
 * </p>
 *
 * @author Lukundo Kileha (kileha3)
 *         lkileha@furahitech.co.tz
 */
public class PaymentRequest implements Serializable{

   /* Merchant details*/
    private String tigoMerchantNumber;
    private String tigoMerchantKey;
    private String tigoMerchantSecret;
    private String tigoMerchantName;
    private String tigoMerchantPin;
    private String cardMerchantKey;
    private String cardMerchantSecret;
    private String wazoHubClientID;
    private String wazoHubClientSecret;

   /* Transaction details*/
    private String transactionID;
    private int transactionFee=0;
    private int transactionTax=0;
    private int transactionAmount=0;

    private String paymentType;
    private String paymentDesc;

   /* Default setups*/
    private String defaultLanguage= SWAHILI;
    private String defaultCountryCode="255";
    private String defaultCountryName="TZA";
    private String defaultCurrency=TSH;

    /*Customer details*/
    private String customerPhone;
    private String customerCard;
    private String customerFirstName;
    private String customerLastName;
    private String customerEmailAddress;

    /*Payment request and log end-points*/
    private String paymentRequestEndPoint;
    private String paymentLogsEndPoint;

    private HashMap<String,String> paymentExtraParam;


    public String getTigoMerchantNumber() {
        return tigoMerchantNumber;
    }

    /**
     * Responsible for setting up Tigo pesa merchant number as acquired from Tigo pesa office
     * @param tigoMerchantNumber Merchant number
     */
    public void setTigoMerchantNumber(String tigoMerchantNumber) {
        this.tigoMerchantNumber = tigoMerchantNumber;
    }

    public String getTigoMerchantKey() {
        return tigoMerchantKey;
    }

    /**
     * Responsible for setting up Tigo pesa merchant security key
     * @param tigoMerchantKey merchant security key
     */
    public void setTigoMerchantKey(String tigoMerchantKey) {
        this.tigoMerchantKey = tigoMerchantKey;
    }

    public String getTigoMerchantSecret() {
        return tigoMerchantSecret;
    }

    /**
     * Responsible for setting up Tigo-Pesa merchant secret word
     * @param tigoMerchantSecret secret word
     */
    public void setTigoMerchantSecret(String tigoMerchantSecret) {
        this.tigoMerchantSecret = tigoMerchantSecret;
    }

    public String getTigoMerchantName() {
        return tigoMerchantName;
    }

    /**
     * Responsible for setting up Tigo-Pesa merchant name
     * @param tigoMerchantName merchant name
     */
    public void setTigoMerchantName(String tigoMerchantName) {
        this.tigoMerchantName = tigoMerchantName;
    }

    public String getTigoMerchantPin() {
        return tigoMerchantPin;
    }

    /**
     * Responsible for setting up Tigo pesa merchant PIN
     * @param tigoMerchantPin security PIN
     */
    public void setTigoMerchantPin(String tigoMerchantPin) {
        this.tigoMerchantPin = tigoMerchantPin;
    }

    public String getTransactionID() {
        return transactionID;
    }

    /**
     * Responsible for setting up transaction reference ID
     * @param transactionID transaction ID
     */
    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public int getTransactionFee() {
        return transactionFee;
    }

    /**
     * Responsible for setting up transaction fee (TigoPesa policy)
     * @param transactionFee transaction fee
     */
    public void setTransactionFee(int transactionFee) {
        this.transactionFee = transactionFee;
    }

    public int getTransactionTax() {
        return transactionTax;
    }

    /**
     * Responsible for setting up transaction tax (TigoPesa policy)
     * @param transactionTax transaction tax
     */
    public void setTransactionTax(int transactionTax) {
        this.transactionTax = transactionTax;
    }

    public int getTransactionAmount() {
        return transactionAmount;
    }

    /**
     * Responsible for setting up amount to be deducted from clients account
     * @param transactionAmount transaction amount
     */
    public void setTransactionAmount(int transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    /**
     * Responsible for setting up default language to be used during transaction, default language is Swahili
     * @param defaultLanguage language code
     */
    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public String getDefaultCountryCode() {
        return defaultCountryCode;
    }

    /**
     * Responsible fro setting up default country code which is TZ by default
     * @param defaultCountryCode country code
     */
    public void setDefaultCountryCode(String defaultCountryCode) {
        this.defaultCountryCode = defaultCountryCode;
    }

    public String getDefaultCountryName() {
        return defaultCountryName;
    }

    /**
     * Responsible for setting up country name
     * @param defaultCountryName  country name
     */
    public void setDefaultCountryName(String defaultCountryName) {
        this.defaultCountryName = defaultCountryName;
    }

    public String getDefaultCurrency() {
        return defaultCurrency;
    }

    /**
     * Responsible for setting up default currency to be used during transaction, by default TZS is currency
     * @param defaultCurrency currency code
     */
    public void setDefaultCurrency(String defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    /**
     * Responsible to set customer phone number on Mobile payments
     * @param customerPhone Phone number
     */
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }


    public String getCustomerCard() {
        return customerCard;
    }

    /**
     * Responsible for setting up customer's credit card on Card payments
     */
    public void setCustomerCard(String customerCard) {
        this.customerCard = customerCard;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    /**
     * Responsible for setting up customer first name
     * @param customerFirstName Customer name
     */
    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    /**
     * Responsible for setting up customer's last name
     * @param customerLastName Customer name
     */
    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public String getCustomerEmailAddress() {
        return customerEmailAddress;
    }

    /**
     * Responsible for setting up customer's email address
     * @param customerEmailAddress Customer email address
     */
    public void setCustomerEmailAddress(String customerEmailAddress) {
        this.customerEmailAddress = customerEmailAddress;
    }

    public String getPaymentType() {
        return paymentType;
    }

    /**
     * Responsible for setting up payment tye (M-Pesa, TigoPesa, Visa, Mastercard etc)
     * @param paymentType Type name
     */
    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentDesc() {
        return paymentDesc;
    }

    /**
     * Responsible for setting up payment description
     * @param paymentDesc Description message
     */
    public void setPaymentDesc(String paymentDesc) {
        this.paymentDesc = paymentDesc;
    }

    public String getPaymentRequestEndPoint() {
        return paymentRequestEndPoint;
    }

    /**
     * Responsible for setting up where all payment request will be sent
     * @param paymentRequestEndPoint URL formatted end-point
     */
    public void setPaymentRequestEndPoint(String paymentRequestEndPoint) {
        this.paymentRequestEndPoint = paymentRequestEndPoint;
    }

    public String getWazoHubClientID() {
        return wazoHubClientID;
    }

    /**
     * Responsible for setting up WazoHub clients ID
     * @param wazoHubClientID merchant ID
     */
    public void setWazoHubClientID(String wazoHubClientID) {
        this.wazoHubClientID = wazoHubClientID;
    }

    public String getWazoHubClientSecret() {
        return wazoHubClientSecret;
    }

    /**
     * Responsible for setting up WazoHub security secret word
     * @param wazoHubClientSecret secret word
     */
    public void setWazoHubClientSecret(String wazoHubClientSecret) {
        this.wazoHubClientSecret = wazoHubClientSecret;
    }

    public String getPaymentLogsEndPoint() {
        return paymentLogsEndPoint;
    }

    /**
     * Responsible for setting up where your partial payment will be logged to wait for a callback
     * @param paymentLogsEndPoint URL formatted en-point
     */
    public void setPaymentLogsEndPoint(String paymentLogsEndPoint) {
        this.paymentLogsEndPoint = paymentLogsEndPoint;
    }

    public String getCardMerchantKey() {
        return cardMerchantKey;
    }

    /**
     * Responsible for setting up Card processor merchant security key
     * @param cardMerchantKey security key
     */
    public void setCardMerchantKey(String cardMerchantKey) {
        this.cardMerchantKey = cardMerchantKey;
    }

    public String getCardMerchantSecret() {
        return cardMerchantSecret;
    }

    /**
     * Responsible for setting up card process merchant secret word
     * @param cardMerchantSecret secret work
     */
    public void setCardMerchantSecret(String cardMerchantSecret) {
        this.cardMerchantSecret = cardMerchantSecret;
    }

    public HashMap<String, String> getPaymentExtraParam() {
        return paymentExtraParam;
    }

    public void setPaymentExtraParam(HashMap<String,String> paymentExtraParam) {
        this.paymentExtraParam = paymentExtraParam;
    }
}
