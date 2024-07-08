package com.ite.ecommerceservice.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {
    private String productId;
    private String categoryId;
    private String productName;
    private String productBrand;
    private long price;
    private String urlImage;
    private int quantity;
}
