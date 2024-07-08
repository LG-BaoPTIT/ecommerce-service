package com.ite.ecommerceservice.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payment_status")
public class PaymentStatus {
    @Id
    private String id;

    @Indexed(unique = true)
    private String code;

    private String description;
}
