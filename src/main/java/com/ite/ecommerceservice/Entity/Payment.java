package com.ite.ecommerceservice.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payments")
public class Payment {
    @Id
    private String id;
    @DBRef
    private List<String> InvoiceId; // Mã đơn hàng
    private String paymentMethod; // Phương thức thanh toán (thẻ tín dụng, COD, chuyển khoản, vv.)
    private double amount; // Số tiền thanh toán
    private String status; // Trạng thái thanh toán (đã thanh toán, đang xử lý, thất bại, vv.)
    private String createdAt;
}
