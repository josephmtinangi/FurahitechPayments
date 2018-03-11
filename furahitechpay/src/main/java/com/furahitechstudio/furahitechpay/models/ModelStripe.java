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
 * <h1>ModelStripe</h1>
 * <p>
 *     ModelStripe is responsible for holding all card payment status details from Stripe card processor after payment task completion
 * </p>
 *
 * @author Lukundo Kileha (kileha3)
 *         lkileha@furahitech.co.tz
 */

public class ModelStripe {

    @SerializedName("status")
    private String status;
    @SerializedName("reason")
    private String reason;
    @SerializedName("risk")
    private String risk;
    @SerializedName("message")
    private String message;
    @SerializedName("type")
    private String type;
    @SerializedName("paid")
    private boolean paid;
    @SerializedName("id")
    private String id;
    @SerializedName("card")
    private String card;
    @SerializedName("amount")
    private int amount;
    @SerializedName("created")
    private int created;
    @SerializedName("country")
    private String country;

    /**
     * Responsible for getting network operation status
     * @return String: Status (approved_by_network, authorized ect)
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Responsible for getting failure reason in case
     * @return String: Reason
     */
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Responsible for getting risk associated with the transaction
     * @return String :Risk
     */
    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }
    /**
     * Responsible for gettting extra message associated with a transaction
     * @return String: Message
     */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Responsible for getting card type
     * @return String: Card type (Visa,Mastercard etc)
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Responsible to get payment status (TRUE if completed successfully otherwise FALSE)
     * @return boolean: Payment status
     */
    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    /**
     * Responsble for getting transaction ID
     * @return String:Transaction ID
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Responsible for getting customer's card number
     * @return String: Card number
     */
    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    /**
     * Responsible for getting transaction amount
     * @return int: Deducted amount from client account
     */
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Responsible for getting transaction completion time
     * @return int: Timestamp
     */
    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    /**
     * Responsible for getting transaction country of origin
     * @return String: Country name
     */
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
