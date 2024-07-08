package com.ite.ecommerceservice.Repository;

import com.ite.ecommerceservice.Entity.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Products,String> {
    boolean existsById(String id);

    Products getProductsById(String productId);

    Page<Products> findAll(Pageable pageable);
    Page<Products> getProductsByCategoryId(String categoryId, Pageable pageable);

    @Query("{ 'categoryId': { $in: ?0 } }")
    Page<Products> findAllByCategoryIds(List<String> categoryIds,Pageable pageable);

    @Query("{$or:[{'productBrand':{$regex:?0,$options:'i'}}, {'productName':{$regex:?0,$options:'i'}}]}")
    Page<Products> findByBrandOrName(String keyword, Pageable pageable);

    @Query("{ 'categoryId': ?0, 'price': { $gte: ?1, $lte: ?2 } }")
    Page<Products> findAllByCategoryIdAndPriceRange(String categoryId, long minPrice, long maxPrice, Pageable pageable);

    @Query("{ 'categoryId': { $in: ?0 }, 'price': { $gte: ?1, $lte: ?2 } }")
    Page<Products> findAllByCategoryIdsAndPriceRange(List<String> categoryIds, long minPrice, long maxPrice, Pageable pageable);

    @Query("{ 'price': { $gte: ?0, $lte: ?1 } }")
    Page<Products> findAllByPriceRange(long minPrice, long maxPrice, Pageable pageable);

    @Query("{ 'categoryId': ?0, 'price': { $gte: ?1 } }")
    Page<Products> findAllByCategoryIdAndMinPrice(String categoryId, long minPrice, Pageable pageable);

    @Query("{ 'categoryId': { $in: ?0 }, 'price': { $gte: ?1 } }")
    Page<Products> findAllByCategoryIdsAndMinPrice(List<String> categoryIds, long minPrice, Pageable pageable);

    @Query("{ 'price': { $gte: ?0 } }")
    Page<Products> findAllByMinPrice(long minPrice, Pageable pageable);
}
