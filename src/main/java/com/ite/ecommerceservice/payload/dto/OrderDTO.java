package com.ite.ecommerceservice.payload.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ite.ecommerceservice.Entity.DeliveryMode;
import com.ite.ecommerceservice.Entity.OrderStatus;
import com.ite.ecommerceservice.Entity.PaymentMethod;
import com.ite.ecommerceservice.Entity.PaymentStatus;
import com.ite.ecommerceservice.data.TransactionInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private String id;
    private String receiverName;
    private String receiverPhone;
    private String userId;
    private long totalAmount;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone="Asia/Ho_Chi_Minh")
    private Instant createdAt;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone="Asia/Ho_Chi_Minh")
    private Instant canceledAt;

    private OrderStatusDTO orderStatus;
    private PaymentStatusDTO paymentStatus;
    private String address;
    private TransactionInfo transactionInfo;
    private DeliveryMode deliveryMode;
    private PaymentMethod paymentMethod;

}
