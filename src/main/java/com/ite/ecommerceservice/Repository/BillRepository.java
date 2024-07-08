package com.ite.ecommerceservice.Repository;


import com.ite.ecommerceservice.Entity.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
//@Repository
public interface BillRepository extends MongoRepository<Bill,String> {
    Optional<Bill> findByOrderId(String orderId);

    List<Bill> findBillByOrderId(String orderId);

//    List<Bill> findBillByStatus(Billstatus billstatus);

    List<Bill> findBillBystatus(String status);

    List<Bill> findByOrderIdOrEmail(String orderId, String email);
    Page<Bill> findAll(Pageable pageable);

    Page<Bill> findByOrderIdOrEmail(String orderId, String email,Pageable pageable);

//    List<Bill> findByOrderIdOrNameOrProductAndStatus(String orderId, String name, String product,Billstatus status);

//    List<Bill> findByOrderIdOrNameOrProduct(String orderId, String name, String product);


//    List<Bill> findBillByStatus(String done);
}
