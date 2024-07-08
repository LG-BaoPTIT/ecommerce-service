package com.ite.ecommerceservice.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bill")
public class Bill {

        @Id
        private String Id;

        private String orderId;
        private String email;
        private String total_price;
        private String payment_method;
        private String status;
        private String shipping_address;
        private Date createdAt;

}
