package com.ite.ecommerceservice.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "order_item")
public class OrderItem {
    @Id
    private String id;
    private String orderId;
    private String productId;
    private int quantity;
    private long price;
}
