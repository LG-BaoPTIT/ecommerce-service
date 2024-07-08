package com.ite.ecommerceservice.Model;

//import com.example.ite.Entity.Invoice;

import com.ite.ecommerceservice.Entity.Bill;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class Mapper {
    public static BillDTOresponse tobillDTOresponse(Bill bill){
        return BillDTOresponse.builder()
                .billId(bill.getId())
                .orderId(bill.getOrderId())
                .email(bill.getEmail())
                .total_price(bill.getTotal_price())
                .payment_method(bill.getPayment_method())
                .status(bill.getStatus())
                .shipping_address(bill.getShipping_address())
                .createdAt(bill.getCreatedAt())
                .build();
    }
}
