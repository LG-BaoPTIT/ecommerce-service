package com.ite.ecommerceservice.Model;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class PaymentDto {
//    private List<String> InvoiceId; // Mã đơn hàng
    private String paymentMethod; // Phương thức thanh toán (thẻ tín dụng, COD, chuyển khoản, vv.)
    private double amount; // Số tiền thanh toán
    private String status; // Trạng thái thanh toán (đã thanh toán, đang xử lý, thất bại, vv.)
    private String createdAt;
}
