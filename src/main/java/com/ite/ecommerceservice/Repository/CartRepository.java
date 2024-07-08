package com.ite.ecommerceservice.Repository;

import com.ite.ecommerceservice.Entity.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CartRepository extends MongoRepository<Cart,String> {
    Cart getCartByUserId(String userId);
}
