package com.ite.ecommerceservice.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchProductRequest {
    private String keyWord;
    private int page;
    private int size;
}
