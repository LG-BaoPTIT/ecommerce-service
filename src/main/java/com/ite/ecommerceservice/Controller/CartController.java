package com.ite.ecommerceservice.Controller;

import com.ite.ecommerceservice.Service.CartService;
import com.ite.ecommerceservice.constants.MessageConstant;
import com.ite.ecommerceservice.payload.request.AddToCartRequest;
import com.ite.ecommerceservice.payload.request.UpdateCartRequest;
import com.ite.ecommerceservice.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/ecommerce/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/add-to-cart")
    public ResponseEntity<?> addToCart(@RequestBody AddToCartRequest addToCartRequest, HttpServletRequest request){
        try{
            return cartService.addToCart(addToCartRequest,request);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/my-cart")
    public ResponseEntity<?> getCartDetail(HttpServletRequest request){
        try{
            return cartService.getCartDetail(request);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/delete-product")
    public ResponseEntity<?> deleteProductInCart(@RequestParam("productId") String productId,HttpServletRequest request){
        try{
            return cartService.deleteProduct(productId,request);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateCart(@RequestBody UpdateCartRequest updateCartRequest,HttpServletRequest request){
        try{
            return cartService.updateCart(updateCartRequest,request);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
