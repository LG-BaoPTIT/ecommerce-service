package com.ite.ecommerceservice.Service;

import com.ite.ecommerceservice.Entity.Order;
import com.ite.ecommerceservice.Repository.OrderRepository;
import com.ite.ecommerceservice.payload.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface OrderService {

    Order save(Order order);

    ResponseEntity<?> createOrder(OrderRequest orderRequest, HttpServletRequest request);

    ResponseEntity<?> getOrder(String orderId, HttpServletRequest request);
}
