package com.ite.ecommerceservice.payload.request;

import com.ite.ecommerceservice.payload.dto.OrderItemDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    public OrderInfo orderInfo;
    public List<OrderItemDTO> orderItemInfo;
    public String ipAddress;
}
