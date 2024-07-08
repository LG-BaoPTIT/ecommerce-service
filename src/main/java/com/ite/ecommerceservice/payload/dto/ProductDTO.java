package com.ite.ecommerceservice.payload.dto;

import com.ite.ecommerceservice.Entity.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private String id;
    private String categoryId;
    private String productName;
    private String productBrand;
    private long oldPrice;
    private long price;
    private int quanlity;
    private boolean isActive;
    private boolean isBestseller;
    private List<String> urlImage;
    private ProductStatus productStatus;
    private String description;

}
