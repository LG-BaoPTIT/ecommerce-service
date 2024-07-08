package com.ite.ecommerceservice.Repository;

import com.ite.ecommerceservice.Entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends MongoRepository<Category,String> {
   Category getCategoryById(String id);
}
