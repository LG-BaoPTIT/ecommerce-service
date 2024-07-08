package com.ite.ecommerceservice.Service.ServiceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ite.ecommerceservice.Entity.*;
import com.ite.ecommerceservice.Repository.*;
import com.ite.ecommerceservice.Service.OrderService;
import com.ite.ecommerceservice.Service.PaymentService;
import com.ite.ecommerceservice.Service.RedisService;
import com.ite.ecommerceservice.constants.MessageConstant;
import com.ite.ecommerceservice.data.PaymentResponseBody;
import com.ite.ecommerceservice.payload.dto.OrderDTO;
import com.ite.ecommerceservice.payload.request.OrderInfo;
import com.ite.ecommerceservice.payload.dto.OrderItemDTO;
import com.ite.ecommerceservice.payload.request.OrderRequest;
import com.ite.ecommerceservice.utils.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private PaymentStatusRepository paymentStatusRepository;

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public ResponseEntity<?> createOrder(OrderRequest orderRequest, HttpServletRequest request) {
        try{

            User user = userRepository.findUserByEmail(request.getHeader("email"));
            if (user == null) {
                return ResponseUtil.getResponseEntity(MessageConstant.USER_NOT_EXIST, HttpStatus.NOT_FOUND);
            }

            OrderInfo orderInfo = orderRequest.getOrderInfo();
            if (Objects.isNull( orderInfo)) {
                return ResponseUtil.getResponseEntity("Order information is missing", HttpStatus.BAD_REQUEST);
            }

            if(orderInfo.getDeliveryMode().equals(DeliveryMode.DELIVERY)){
                if (StringUtils.isBlank(orderInfo.getReceiverName()) || StringUtils.isBlank(orderInfo.getAddress())||StringUtils.isBlank(orderInfo.getReceiverPhone()) || Objects.isNull(orderInfo.getDeliveryMode()) || Objects.isNull(orderInfo.getPaymentMethod())) {
                    return ResponseUtil.getResponseEntity("Some fields in order information are missing or empty", HttpStatus.BAD_REQUEST);
                }
            }

            List<OrderItemDTO> orderItems = orderRequest.getOrderItemInfo();
            if (Objects.isNull(orderItems)) {
                return ResponseUtil.getResponseEntity("Order item information is missing", HttpStatus.BAD_REQUEST);
            }

            long totalAmount = 0;
            for (OrderItemDTO item : orderItems) {
                Products product = productRepository.findById(item.getProductId()).orElse(null);
                totalAmount += item.getPrice() * item.getQuantity();
                if (product == null) {
                    return ResponseUtil.getResponseEntity("Product with ID " + item.getProductId() + " not found", HttpStatus.NOT_FOUND);
                }

                if (item.getPrice() != product.getPrice()) {
                    return ResponseUtil.getResponseEntity("Price mismatch for product ID " + item.getProductId(), HttpStatus.BAD_REQUEST);
                }

                if (item.getQuantity() > product.getQuanlity()) {
                    return ResponseUtil.getResponseEntity("Insufficient stock for product ID " + item.getProductId(), HttpStatus.BAD_REQUEST);
                }
            }

            Order order = modelMapper.map(orderInfo,Order.class);
            order.setCreatedAt(Instant.now());
            order.setTotalAmount(totalAmount);
            order.setUserId(user.getUserId());
            if(orderInfo.getDeliveryMode().equals(DeliveryMode.IN_STORE)){
                order.setReceiverName(user.getName());
                order.setReceiverPhone(user.getPhone());
            }
            order.setOrderStatus(orderStatusRepository.getOrderStatusesByCode("00"));
            order.setPaymentStatus(paymentStatusRepository.getPaymentStatusesByCode("01"));
            orderRepository.save(order);


            for(OrderItemDTO item : orderItems){
               // OrderItem orderItem =modelMapper.map(item,OrderItem.class);
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(order.getId());
                orderItem.setProductId(item.getProductId());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setPrice(item.getPrice());
                orderItemRepository.save(orderItem);
            }

            Cart cart = cartRepository.getCartByUserId(user.getUserId());
            for (OrderItemDTO item : orderItems) {
                cartItemRepository.deleteByCartIdAndProductId(cart.getId(),item.getProductId());

                Products product = productRepository.findById(item.getProductId()).orElse(null);
                product.setQuanlity(product.getQuanlity() - item.getQuantity());
                if(product.getQuanlity() == 0){
                    product.setProductStatus(ProductStatus.OUT_OF_STOCK);
                }
                productRepository.save(product);
            }
            if(orderRequest.getOrderInfo().getPaymentMethod().equals(PaymentMethod.ONLINE_PAYMENT)){
                ResponseEntity<?> paymentResponse = paymentService.sendPaymentRequest(request,String.valueOf(totalAmount), orderRequest.ipAddress, user.getPhone(),order.getId());
                PaymentResponseBody paymentResponseBody =  objectMapper.readValue( paymentResponse.getBody().toString(), PaymentResponseBody.class);
                HttpStatus httpStatus = paymentResponse.getStatusCode();
                if(httpStatus.is4xxClientError()){
                    return ResponseUtil.getResponseEntity(MessageConstant.UNAUTHORIZED,HttpStatus.UNAUTHORIZED);
                }
                if(paymentResponseBody.getError_code().equals("00")){
                    order.setPaymentStatus(paymentStatusRepository.getPaymentStatusesByCode("02"));
                    order.setCreatedAt(Instant.now());
                    orderRepository.save(order);
                    redisService.set("orderId:"+order.getId(),order.getId());
                    redisService.setTimeToLive("orderId:"+order.getId(),16);
                    return new ResponseEntity<String>("{\"Payment_url\":\""+paymentResponseBody.getPayment_url()+"\"}",httpStatus);
                }else {
                    return ResponseUtil.getResponseEntity(MessageConstant.INVALID_DATA,HttpStatus.BAD_REQUEST);
                }
            }
            return ResponseUtil.getResponseEntity(MessageConstant.ORDER_SUCCESSFULLY, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getOrder(String orderId, HttpServletRequest request) {
        try {
            if(!orderRepository.existsById(orderId)){
                return ResponseUtil.getResponseEntity(MessageConstant.ORDER_NOT_EXIST, HttpStatus.NOT_FOUND);
            }
            Order order = orderRepository.getOrderById(orderId);
            OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
            return ResponseEntity.ok(orderDTO);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
