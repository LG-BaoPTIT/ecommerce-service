package com.ite.ecommerceservice.Controller;

import com.ite.ecommerceservice.Service.OrderService;
import com.ite.ecommerceservice.constants.MessageConstant;
import com.ite.ecommerceservice.payload.request.OrderRequest;
import com.ite.ecommerceservice.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/ecommerce/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest, HttpServletRequest request){
        try{
            return orderService.createOrder(orderRequest, request);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getOrder")
    public ResponseEntity<?> getOrder(@RequestParam("orderId") String orderId, HttpServletRequest request){
        try{
            return orderService.getOrder(orderId, request);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/test")
    public String test(@RequestBody String str){
        System.out.println(str);
        return "test hihi 123!";
    }


}
