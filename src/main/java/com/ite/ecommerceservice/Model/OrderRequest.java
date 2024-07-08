package com.ite.ecommerceservice.Model;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OrderRequest {
    private String users;
    private String Products;
    private String TotalPrice;
    private String ShippingAddress;


}
