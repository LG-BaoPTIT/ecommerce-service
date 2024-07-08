package com.ite.ecommerceservice.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class Products {
    @Id
    private String id;
    private String productName;

    private String categoryId;
    private String productBrand;
    private long oldPrice;
    private long price;
    private int quanlity;
    private boolean isActive;
    private boolean isBestseller;
    private List<String> urlImage;
    private String description;
    private ProductStatus productStatus;
    private Instant createAt;
    private Instant updateAt;
}
