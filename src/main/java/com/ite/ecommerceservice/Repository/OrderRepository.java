package com.ite.ecommerceservice.Repository;

import com.ite.ecommerceservice.Entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<Order,String> {
    Order getOrderById(String orderId);
    boolean existsByIdAndUserId(String id, String userId);
    boolean existsById(String id);
}
