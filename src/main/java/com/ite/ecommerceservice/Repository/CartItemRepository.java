package com.ite.ecommerceservice.Repository;

import com.ite.ecommerceservice.Entity.CartItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends MongoRepository<CartItem,String> {
    List<CartItem> getAllByCartId(String cartId);

    Optional<CartItem> findByCartIdAndProductId(String id, String productId);

    boolean existsByCartIdAndProductId(String cartId, String productId);

    void deleteByCartIdAndProductId(String id, String productId);
}
