package com.ite.ecommerceservice.Model;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class OrderDTO {
    private String invoiceId;
//    private List<String> productIds;
    private double totalPrice;
    private String status;
    private String createdAt;
    private PaymentDto paymentDto;
    private ProductDto productDto;
}
