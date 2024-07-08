package com.ite.ecommerceservice.Service;

import com.ite.ecommerceservice.payload.request.AddToCartRequest;
import com.ite.ecommerceservice.payload.request.UpdateCartRequest;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface CartService {
    ResponseEntity<?> addToCart(AddToCartRequest addToCartRequest, HttpServletRequest request);

    ResponseEntity<?> getCartDetail(HttpServletRequest request);

    ResponseEntity<?> deleteProduct(String productId,HttpServletRequest request);

    ResponseEntity<?> updateCart(UpdateCartRequest updateCartRequest,HttpServletRequest request);

    void deleteItemInCart(String userId,String productId);
}
