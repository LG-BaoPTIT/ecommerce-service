package com.ite.ecommerceservice.Model;

import lombok.*;

import java.time.Instant;
@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class ProductDto {
    private String productId;
    private String product_name;
    private String product_brand;
    private long old_price;
    private boolean is_active;
    private boolean is_bestseller;
    private int quanlity;
    private String description;
    private Instant create_at;
    private Instant update_at;
}
