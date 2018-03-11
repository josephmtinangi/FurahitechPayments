package com.furahitechstudio.furahitechpay.models;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
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
 * <h1>ModelWazoHub</h1>
 * <p>
 *     ModelWazoHub is responsible for holding all wazoHub authentication request response
 * </p>
 *
 * @author Lukundo Kileha (kileha3)
 *         lkileha@furahitech.co.tz
 */
public class ModelWazoHub {
    /**
     * Class responsible for authentication response from WazoHub
     */
    public static class AuthenticationResponse implements Serializable {
        @SerializedName("token_type")
        private String token_type;

        @SerializedName("expires_in")
        private int expires_in;

        @SerializedName("access_token")
        private String access_token;

        /**
         * Responsible for getting type of the token request (CREDENTIALS)
         * @return String: Token type
         */
        public String getToken_type() {
            return token_type;
        }

        /**
         * Responsible for getting expiration time of the token
         * @return int: time in minutes
         */
        public int getExpires_in() {
            return expires_in;
        }

        /**
         * Responsible for getting authentication token from WazoHub
         * @return String: Token
         */
        public String getAccess_token() {
            return access_token;
        }
    }

    /**
     * Class which is responsible for transaction request response from WazoHub
     */
    public static class TransactionResponse implements Serializable{
        @SerializedName("uid")
        private String uid;
        @SerializedName("code")
        private String code;
        @SerializedName("desc")
        private String desc;
        @SerializedName("extra")
        private Extra extra;

        /**
         * Responsible for getting trasantion UID which will be sent to vodacom
         * as account number identification
         * @return String: Transaction Identification
         */
        public String getUid() {
            return uid;
        }

        /**
         * Responsible for getting process HTTP status code
         * @return String: HTTP code (100 - continue,200 - OK ,105 - Failed,404 - Not found)
         */
        public String getCode() {
            return code;
        }

        /**
         * Responsible for getting payment description
         * @return String: Description
         */
        public String getDesc() {
            return desc;
        }

        /**
         * Responsible for getting extra param (Redirection URL) in case of TigoPesa
         * @return Extra: Object
         */
        public Extra getExtra() {
            return extra;
        }

        public static class Extra {
            @SerializedName("redirectUri")
            private String redirectUri;

            /**
             * Responsible for getting URL to tigopesa secure page
             * @return String: URL
             */
            public String getRedirectUri() {
                return redirectUri;
            }
        }
    }
}
