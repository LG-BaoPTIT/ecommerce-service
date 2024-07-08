package com.ite.ecommerceservice.Controller;

import com.ite.ecommerceservice.Service.PaymentService;
import com.ite.ecommerceservice.constants.MessageConstant;
import com.ite.ecommerceservice.data.TransactionInfo;
import com.ite.ecommerceservice.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.PublicKey;

@CrossOrigin
@RestController
@RequestMapping("/ecommerce/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/handleResult")
    public ResponseEntity<?> handlePaymentResult(@RequestBody String data){
        System.out.println("body:"+data);
        return ResponseEntity.ok("");
    }

    @GetMapping("/test")
    public ResponseEntity<?> test(@RequestParam("orderId")String orderId){
        TransactionInfo transactionInfo = paymentService.getTransactionInfo(orderId);
        return ResponseEntity.ok("");
    }
    @GetMapping("/test2")
    public ResponseEntity<?> test2(){
        return ResponseEntity.ok("123123");
    }

    @GetMapping("/getResult")
    public ResponseEntity<?> getPaymentResult(@RequestParam("orderId") String orderId, HttpServletRequest request){
        try{
            return paymentService.getPaymentResult(request, orderId);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
