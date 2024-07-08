package com.ite.ecommerceservice.Repository;

import com.ite.ecommerceservice.Entity.PaymentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentStatusRepository extends MongoRepository<PaymentStatus, String> {
    PaymentStatus getPaymentStatusesByCode(String code);
}
