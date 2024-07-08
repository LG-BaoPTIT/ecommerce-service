package com.ite.ecommerceservice.Repository;

import com.ite.ecommerceservice.Entity.OrderItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface OrderItemRepository extends MongoRepository<OrderItem, String> {

}
