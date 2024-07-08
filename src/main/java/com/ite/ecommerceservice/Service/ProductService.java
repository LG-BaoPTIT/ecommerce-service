package com.ite.ecommerceservice.Service;

import com.ite.ecommerceservice.payload.request.SearchProductRequest;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ProductService {
    ResponseEntity<?> getProducts(HttpServletRequest request, String category,String subCategory,Long minPrice,Long maxPrice, Integer page, Integer size);

    ResponseEntity<?> getProductDetail(String productId,HttpServletRequest request);

    ResponseEntity<?> search(SearchProductRequest searchProductRequest, HttpServletRequest request);

    ResponseEntity<?> getProductByFilter(HttpServletRequest request, long priceStart, long priceEnd);
}
