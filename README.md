# FurahitechPay
<p>
This is a collection of different payment API's, this API supports Stripe, Tigo Pesa and M-Pesa payments for now. For M-Pesa payments we have used wazoHub as our aggregator.
</p>

## Releases <a name="releases"></a>


## Features <a name="features"></a>
* Small library
* Supports Stripe,M-Pesa and Tigo Pesa payments
* Select only supported mobile network operators on mobile payments
* Automatically monitor for the callback on the server side (M-Pesa payments)
* Oprate on both LIVE and SANBOX environments.
* Check payment status from the Tigo Secure page

## Installation <a name="installation"></a>
This API depends on server side logic which help to log partial payments to a JSON file waiting for a callback from M-Pesa side and payment authorisation from Tigo Pesa side.

### Server side installation

This API work with <a href="https://github.com/kileha3/PaymentGatewayAPIs-PHP">PaymentGatewayAPIs-PHP</a>, what you have to do is:-<br/>
* Clone PaymentGatewayAPIs-PHP from <a href="https://github.com/kileha3/PaymentGatewayAPIs-PHP">here</a>
* Upload it to your hosting service
* Submit your callback URL to WazoHub as:-

```html
 exampledomain.com/v1/callback
```

### Client Side installation (Android)
Add this to your dependencies (Check for the lastest release):
```groovy
def versionNumber = "v1.0"

compile "com.github.kileha3:FurahitechPay:$versionNumber"
```

Initialize payment request (Card Payment):
```java
 PaymentRequest request=new PaymentRequest();
 
 /*For card merchant credentials */
 request.setCardMerchantKey("");
 request.setCardMerchantSecret("");
 
 /*Customer details*/
 request.setCustomerEmailAddress("");
 request.setCustomerFirstName("");
 request.setCustomerLastName("");
 
 /*Transaction other details*/
 request.setTransactionAmount(0);
 request.setTransactionFee(0);
 request.setPaymentDesc("Paying amount X for X reason");
 
 /*HTTP request endPoints*/
 request.setPaymentRequestEndPoint("");
 
 /*Build request*/
  FurahitechPay.getInstance()
                  .with(this)
                  .setPaymentRequest(request)
                  .setPaymentMode(MODE_CARD)
                  .setPaymentEnvironment(LIVE).build();
 
```

Initialize payment request (Mobile Payment - M-PESA and Tigo Pesa):
```java
 PaymentRequest request=new PaymentRequest();
 
 /*Merchant credentials - Tigo Pesa */
 request.setTigoMerchantKey("");
 request.setTigoMerchantSecret("");
 request.setTigoMerchantPin("");
 request.setTigoMerchantNumber("");
 request.setTigoMerchantName("");
 
 /*Merchant credentials - M-Pes*/
  request.setWazoHubClientID("");
  request.setWazoHubClientSecret("");
 
 /*Customer details*/
 request.setCustomerEmailAddress("");
 request.setCustomerFirstName("");
 request.setCustomerLastName("");
 
 /*Transaction other details*/
 request.setTransactionAmount(0);
 request.setTransactionFee(0);
 request.setPaymentDesc("Paying amount X for X reason");
 
 /*HTTP request endPoints*/
 request.setPaymentRequestEndPoint("");
 request.setPaymentLogsEndPoint("");
 
 /*Build request*/
  FurahitechPay.getInstance()
                  .with(this)
                  .setPaymentRequest(request)
                  .setPaymentMode(MODE_MOBILE)
                  .setSupportedGateway(GATEWAY_MPESA,GATEWAY_TIGOPESA)
                  .setPaymentEnvironment(LIVE).build();
 
```
Payment request and log end-point configuration, write your site URL with added "/".It should be the exact location on your server where PaymentGatewayAPIs-PHP files were uploaded.
```html
 exampledomain.com/
```
Passing extra data to the Library for payment process, it might might be product Id or anything
```java
...
 /*Passing extra param*/
 HashMap<String,String> extras=new HashMap<>();
 extras.put("","");
 request.setPaymentExtraParam(extras);
```

In case you don't fully support all mobile payments just pass supported ones.
```java
...
//Remove unsupported ones from parameter list
.setSupportedGateway(GATEWAY_MPESA,GATEWAY_TIGOPESA)
```

For SANDBOX development environment
```java
...
//use SANDBOX as paramter by default it is LIVE
.setPaymentEnvironment(SANDBOX)
```

Get payment status
```java
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE_PAYMENT_STATUS:
                    PaymentStatus status= (PaymentStatus) data.getSerializableExtra(RESULT_PAYMENT_STATUS);
                    break;
            }
        }
    }
```

Sometimes you might want to add this to your AndroidManifest.xml to make API use your app's default theme:
```xml
<application
   tools:replace="tools:theme"
    ...
    >
```

## Contributing <a name="contribute"></a>
* Do you have a new feature in mind?
* Do you know how to improve existing docs or code?
* Have you found a bug?

Feel free to fork this project and send pull request, your contribution will be highly appreciated.Be ready to discuss your code and design logcs

## License <a name="license"></a>

    Copyright (c) 2018 Lukundo Kileha

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sub license, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANT ABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.


