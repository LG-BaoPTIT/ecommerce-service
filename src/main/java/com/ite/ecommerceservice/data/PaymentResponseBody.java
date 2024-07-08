package com.ite.ecommerceservice.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseBody {
    private String session_id;
    private String merchant_id;
    private String amount;
    private String currency;
    private String expire_time;
    private String order_reference;
    private String order_info;
    private String payment_url;
    private String qr_url;
    private String qr_info;
    private String message;
    private String error_code;
}
