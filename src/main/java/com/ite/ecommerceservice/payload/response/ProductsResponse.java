package com.ite.ecommerceservice.payload.response;

import com.ite.ecommerceservice.payload.dto.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductsResponse {
    private List<ProductDTO> products;
    private long totalRecords;
    private int currentPageSize;
}
