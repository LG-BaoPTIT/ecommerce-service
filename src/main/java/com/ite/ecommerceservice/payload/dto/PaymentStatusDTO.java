package com.ite.ecommerceservice.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatusDTO {
    private String id;

    private String code;

    private String description;
}
