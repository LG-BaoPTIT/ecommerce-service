package com.ite.ecommerceservice.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private String sessionId;
    private String merchantId;
    private String amount;
    private String currency;
    private String expireTime;
    private String orderReference;
    private String orderInfo;
    private String paymentUrl;
    private String qrImage;
    private String qrInfo;
}
