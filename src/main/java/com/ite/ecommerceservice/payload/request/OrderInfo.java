package com.ite.ecommerceservice.payload.request;

import com.ite.ecommerceservice.Entity.DeliveryMode;
import com.ite.ecommerceservice.Entity.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfo {

    private String receiverName;
    private String receiverPhone;
    private String address;

    private DeliveryMode deliveryMode;
    private PaymentMethod paymentMethod;
}
