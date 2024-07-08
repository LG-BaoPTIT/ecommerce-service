package com.ite.ecommerceservice.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ite.ecommerceservice.data.TransactionInfo;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "order")
@Builder
public class Order {

    @Id
    private String id;
    private String receiverName;
    private String receiverPhone;
    private String userId;
    private long totalAmount;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone="Asia/Ho_Chi_Minh")
    private Instant createdAt;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone="Asia/Ho_Chi_Minh")
    private Instant canceledAt;

    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private String address;
    private TransactionInfo transactionInfo;
    private DeliveryMode deliveryMode;
    private PaymentMethod paymentMethod;

}
