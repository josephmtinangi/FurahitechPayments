package com.furahitechstudio.furahitechpay.utils;

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
 * <h1>Furahitech</h1>
 * <p>
 *     Furahitech is class defines all constants used in the API
 * </p>
 *
 * @author Lukundo Kileha (kileha3)
 *         lkileha@furahitech.co.tz
 */

public class Furahitech {

    /**
     * Variable which holds the current status check count
     */
    public static int currentRetryCount=0;
    /**
     * Log tag value
     */
    static final String LIB_TAG ="FurahitechPay";

    /**
     * Allowed characters for making transaction reference ID
     */
    static final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";

    /**
     * Tigo secure page redirection URL value TAG
     */
    public static final String PAYMENT_REDIRECTION_PARAM ="payment_redirection_param";

    /**
     * Default customer phone number hint
     */
    public static final String CUSTOM_PHONE_NUMBER_HINT="712000000";

    /**
     * Default customer phone number mask
     */
    public static final String CUSTOM_PHONE_NUMBER_MASK="+255(###) ###-###";

    /**
     * Payment gateway URL's
     */
    public static final String GATEWAY_WAZOHUB_ENDPOINT ="http://18.220.89.57:13201/";

    /**
     * Maximu count for the partial transaction check-up before label transaction as timed out or failure
     */
    public static final int CALLBACK_CHECK_MAX_RETRY_COUNT = 4;

    /**
     * Amount of time to wait before checking for the partial payment status
     */
    public static final int CALLBACK_CHECK_INTERVAL=15;


    /**
     * Payment request data tag from result activity
     */
    public static final String RESULT_PAYMENT_STATUS ="payment_status";

    /**
     * Payment request code for the result activity
     */
    public static final int REQUEST_CODE_PAYMENT_STATUS =900;

    /**
     * Responsible for handing payment statuses
     */
    public enum PaymentConstant {
        STATUS_FAILURE,
        STATUS_TIMEOUT,
        STATUS_CANCELLED,
        STATUS_SUCCESS,
        MODE_CARD,
        MODE_MOBILE,
        GATEWAY_MPESA,
        GATEWAY_TIGOPESA,
        GATEWAY_STRIPE,
        GATEWAY_NONE

    }

    /**
     * Class responsible for handling payment scopes via WazoHub
     */
    public class WazoScopes{
        public static final String SCOPE_MPESA ="mpesa.c2b_push";
    }

    /**
     * Class responsible for API operation environment tags
     */
    public class PaymentEnvironment{
        public static final String SANDBOX="SANDBOX",LIVE="LIVE";
    }

    /**
     * Class for transaction money currency
     */
    public class PaymentCurrency {
        public static final String  TSH="TZS",USD="USD";

    }

    /**
     * Class foe default transaction language, SWA is default
     */
    public class PaymentLanguage {
        public static final String  SWAHILI="swa",ENGLISH="eng";
    }

    /**
     * Class for mobile network operators type
     */
    public class CallBackStatus{
        public static final String RECEIVED="200";
        public static final String WAITING ="100";
        public static final String TIMEOUT ="444";
    }

    /**
     * Class responsible for callback identification state from the server
     */
    public class CallbackState{
        public static final String STATE_RECEIVED="received";
    }

    /**
     * Class for all params to be passed during requests
     */
    class PaymentDataKeys {
        static final String  CUSTOMER_EMAIL="email",CUSTOMER_PHONE="phone", CUSTOMER_FNAME="firstName",CUSTOMER_LNAME="lastName",
                MERCHANT_KEY="apiKey", MERCHANT_SECRET="apiSecret", MERCHANT_ACCOUNT="accountName",MERCHANT_MSIDN="accountMsisdn",
                MERCHANT_PIN="accountPin", TRANS_TAX="tax",TRANS_FEE="fee",TRANS_LANGUAGE="language", TRANS_AMOUNT="amount",
                TRANS_UUID="uuid",TRANS_STATUS_CODE="code",TRANS_REFID="refId",TRANS_REQID="requestId",TRANS_MSID="msisdn",
                OPERATION_ENV="environment",COUNTRY_CODE="countryCode", COUNTRY_NAME="country", DESCRIPTION="description",
                TRANS_TOKEN="token",TRANS_CURRENCY="currency",OPERATOR="operator",WAZO_CLIENT_ID="client_id",
                WAZO_GRANT_TYPE="grant_type",WAZO_CLIENT_SECRET="client_secret",WAZO_SCOPE="scope";
    }



}
