package com.ite.ecommerceservice.redis;

import com.ite.ecommerceservice.Entity.Order;
import com.ite.ecommerceservice.Entity.EPaymentStatus;
import com.ite.ecommerceservice.Repository.OrderRepository;
import com.ite.ecommerceservice.Repository.OrderStatusRepository;
import com.ite.ecommerceservice.Repository.PaymentStatusRepository;
import com.ite.ecommerceservice.Service.PaymentService;
import com.ite.ecommerceservice.data.TransactionInfo;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RedisKeyExpirationListener {
    private final PaymentService paymentService;

    private final OrderRepository orderRepository;

    private final OrderStatusRepository orderStatusRepository;

    private final PaymentStatusRepository paymentStatusRepository;

    public RedisKeyExpirationListener(PaymentService paymentService,OrderRepository orderRepository,OrderStatusRepository orderStatusRepository,PaymentStatusRepository paymentStatusRepository) {
        this.paymentService = paymentService;
        this.orderRepository = orderRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.paymentStatusRepository = paymentStatusRepository;

    }

    public void messageReceived(String message, String channel) {
        try {
            if (message.startsWith("orderId:")) {
                String[] parts = message.split(":");
                String orderId = parts[1];

                System.out.println("Expired order key: " + message + " from channel: " + channel);
                Order order = orderRepository.getOrderById(orderId);
                TransactionInfo transactionInfo =  paymentService.getTransactionInfo(orderId);
                if(!Objects.isNull(transactionInfo) && transactionInfo.getError_code().equals("00")){
                    if(transactionInfo.getStatus().equals("00")){
                        order.setPaymentStatus(paymentStatusRepository.getPaymentStatusesByCode("00"));
                        orderRepository.save(order);

                    }else {
                        if (transactionInfo.getStatus().equals("01")) {
                            order.setPaymentStatus(paymentStatusRepository.getPaymentStatusesByCode("02"));
                            orderRepository.save(order);

                        } else {
                            if (transactionInfo.getStatus().equals("02")) {
                                order.setPaymentStatus(paymentStatusRepository.getPaymentStatusesByCode("03"));
                                orderRepository.save(order);

                            }
                        }
                    }

//                Order order = orderRepository.getOrderById(orderId);
//                order.setPaymentStatus(paymentStatusRepository.getPaymentStatusesByCode("00"));
//                orderRepository.save(order);
                }
                else {
                    order.setPaymentStatus(paymentStatusRepository.getPaymentStatusesByCode("01"));
                    orderRepository.save(order);
                }
                System.out.println(transactionInfo);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
