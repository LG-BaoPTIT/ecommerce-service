package com.ite.ecommerceservice.Model;


import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class BillDTOresponse {
    private String billId;
    private String orderId;
    private String email;
    private String total_price;
    private String payment_method;
    private String status;
    private String shipping_address;
    private Date createdAt;
}
