package com.ite.ecommerceservice.Service;

import com.ite.ecommerceservice.data.TransactionInfo;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface PaymentService {
    ResponseEntity<?> sendPaymentRequest(HttpServletRequest request, String amount,String ipAddress, String orderInfo,String order_reference);

    TransactionInfo getTransactionInfo(String orderId);

    ResponseEntity<?> getPaymentResult(HttpServletRequest request, String orderId);
}
