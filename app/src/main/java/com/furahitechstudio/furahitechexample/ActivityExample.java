package com.furahitechstudio.furahitechexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.furahitechstudio.furahitechpay.FurahitechPay;
import com.furahitechstudio.furahitechpay.models.PaymentRequest;
import com.furahitechstudio.furahitechpay.models.PaymentStatus;

import java.util.HashMap;

import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentConstant.MODE_MOBILE;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentEnvironment.SANDBOX;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.REQUEST_CODE_PAYMENT_STATUS;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.RESULT_PAYMENT_STATUS;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentConstant.GATEWAY_MPESA;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentConstant.GATEWAY_TIGOPESA;
import static com.furahitechstudio.furahitechpay.utils.Furahitech.PaymentEnvironment.LIVE;

public class ActivityExample extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        PaymentRequest request=new PaymentRequest();

        /*For mobile (tigopesa) merchant credentials */
        request.setTigoMerchantKey("");
        request.setTigoMerchantSecret("");
        request.setTigoMerchantPin("");
        request.setTigoMerchantNumber("");
        request.setTigoMerchantName("");

        /*For mobile (mpesa) merchant credentials */
        request.setWazoHubClientID("");
        request.setWazoHubClientSecret("");

        /*For card merchant credentials */
        request.setCardMerchantKey("");
        request.setCardMerchantSecret("");

       /*Customer details*/
        request.setCustomerEmailAddress("");
        request.setCustomerFirstName("Furahitech");
        request.setCustomerLastName("Studio");

        /*Transaction other details*/
        request.setTransactionAmount(100);
        request.setTransactionFee(0);
        request.setPaymentDesc("Payment for demonstration");

        /*HTTP request endPoints*/
        request.setPaymentRequestEndPoint("");
        request.setPaymentLogsEndPoint("");

        /*Passing extra param*/
        HashMap<String,String> extras=new HashMap<>();
        extras.put("_id","XYZQ");
        request.setPaymentExtraParam(extras);

        FurahitechPay.getInstance()
                .with(this)
                .setPaymentRequest(request)
                .setPaymentEnvironment(SANDBOX)
                .setPaymentMode(MODE_MOBILE)
                .setSupportedGateway(GATEWAY_MPESA,GATEWAY_TIGOPESA)
                .setPaymentEnvironment(LIVE).build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE_PAYMENT_STATUS:
                    PaymentStatus status= (PaymentStatus) data.getSerializableExtra(RESULT_PAYMENT_STATUS);
                    Log.d("TAG",String.valueOf(status.isPaidSuccessfully()));
                    break;
            }
        }
    }
}
