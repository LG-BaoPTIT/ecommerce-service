package com.ite.ecommerceservice.Repository;

import com.ite.ecommerceservice.Entity.OrderStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusRepository extends MongoRepository<OrderStatus,String> {
    OrderStatus getOrderStatusesByCode(String code);
}
