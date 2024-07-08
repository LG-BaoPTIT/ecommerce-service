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
@Document(collection = "order_status")
public class OrderStatus {

    @Id
    private String id;

    @Indexed(unique = true)
    private String code;

    private String description;

}
