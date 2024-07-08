package com.ite.ecommerceservice.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "cart_item")
public class CartItem {
    @Id
    private String id;

    private String cartId;

    private String productId;

    private int quantity;

    private Instant createdAt;
}
