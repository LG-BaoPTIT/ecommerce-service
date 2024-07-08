package com.ite.ecommerceservice.Service.ServiceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import com.ite.ecommerceservice.Entity.Order;
import com.ite.ecommerceservice.Entity.User;
import com.ite.ecommerceservice.Repository.OrderRepository;
import com.ite.ecommerceservice.Repository.PaymentStatusRepository;
import com.ite.ecommerceservice.Repository.UserRepository;
import com.ite.ecommerceservice.Service.PaymentService;
import com.ite.ecommerceservice.Service.RedisService;
import com.ite.ecommerceservice.constants.MessageConstant;
import com.ite.ecommerceservice.data.PaymentRequestBody;
import com.ite.ecommerceservice.data.TransactionInfo;
import com.ite.ecommerceservice.data.TransactionRequestBody;
import com.ite.ecommerceservice.utils.PaymentUtil;
import com.ite.ecommerceservice.utils.RequestIdGenerator;
import com.ite.ecommerceservice.utils.ResponseUtil;
import io.jsonwebtoken.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PaymentUtil paymentUtil;

    @Autowired
    private RedisService redisService;

    @Autowired
    private PaymentStatusRepository paymentStatusRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${payment.accessCode}")
    private String accessCode;

    @Value("${payment.hashSecret}")
    private String hashSecret;

    @Value("${payment.cancelUrl}")
    private String cancelUrl;

    @Value("${payment.merchantId}")
    private String merchantId;

    @Value("${payment.ipnUrl}")
    private String ipnUrl;

    @Value("${payment.returnUrl}")
    private String returnUrl;

    @Value("${payment.urlPayment}")
    private String urlPayment;

    @Value("${payment.urlTransactionDetail}")
    private String urlTransaction;

    @Override
    public ResponseEntity<?> sendPaymentRequest(HttpServletRequest request, String amount,String ipAddress, String orderInfo,String order_reference) {

        PaymentRequestBody body = paymentUtil.configBody(amount,ipAddress,orderInfo,order_reference);
        HttpHeaders headers = paymentUtil.configHeader(request,body.toString());

        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> requestEntity = new HttpEntity<>(body.toString(), headers);
        System.out.println("request Body: " + requestEntity.getBody());

        ResponseEntity<String> responseEntity = restTemplate.exchange(urlPayment, HttpMethod.POST, requestEntity, String.class);

        String responseBody = responseEntity.getBody();
        System.out.println("Response Body: " + responseBody);
        if(paymentUtil.checkSignature(responseEntity)){
            return ResponseEntity.status(responseEntity.getStatusCode())
                    .headers(responseEntity.getHeaders())
                    .body(responseEntity.getBody());
        }else {
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @Override
    public TransactionInfo getTransactionInfo(String orderId) {
        if(!orderRepository.existsById(orderId)){
            return null;
        }
        Order order = orderRepository.getOrderById(orderId);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(order.getCreatedAt(), ZoneId.of("UTC"));

        // Định dạng LocalDateTime thành chuỗi theo định dạng yêu cầu
        String payTime = localDateTime.format(DateTimeFormatter.ofPattern("ddMMyyyyHHmmss"));

        // Tạo TransactionRequestBody với pay_time đã định dạng
        TransactionRequestBody transactionRequestBody = TransactionRequestBody.builder()
                .access_code(accessCode)
                .merchant_id(merchantId)
                .order_reference(orderId)
                .pay_time(payTime)
                .version("2.0")
                .build();
        String body = transactionRequestBody.toString();

        HttpHeaders headers = paymentUtil.configHeader(body);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(urlTransaction, HttpMethod.POST, requestEntity, String.class);
        if(!paymentUtil.checkSignature(responseEntity)){
            return null;
        }

        String responseBody = responseEntity.getBody();
        TransactionInfo transactionInfo = new TransactionInfo();
        try {
            transactionInfo = objectMapper.readValue(responseBody, TransactionInfo.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Response Body: " + responseBody);
        return transactionInfo;
    }

    @Override
    public ResponseEntity<?> getPaymentResult(HttpServletRequest request, String orderId) {
        try{
//            if(!userRepository.existsByEmail(request.getHeader("email"))){
//                return ResponseUtil.getResponseEntity(MessageConstant.USER_NOT_EXIST,HttpStatus.BAD_REQUEST);
//            }

            User user = userRepository.findUserByEmail(request.getHeader("email"));
            if(!orderRepository.existsByIdAndUserId(orderId,user.getUserId())){
                return ResponseUtil.getResponseEntity(MessageConstant.INVALID_DATA,HttpStatus.BAD_REQUEST);
            }


            Order order = orderRepository.getOrderById(orderId);
            TransactionInfo transactionInfo = new TransactionInfo();
            if(order.getPaymentStatus().getCode().equals("02")){
                redisService.delete("orderId:"+ orderId);
                transactionInfo = getTransactionInfo(orderId);
                if(transactionInfo.getStatus().equals("00")){
                    order.setPaymentStatus(paymentStatusRepository.getPaymentStatusesByCode("00"));
                }else {
                    if(transactionInfo.getStatus().equals("01")){
                        order.setPaymentStatus(paymentStatusRepository.getPaymentStatusesByCode("02"));
                    }else {
                        if(transactionInfo.getStatus().equals("02")) {
                            order.setPaymentStatus(paymentStatusRepository.getPaymentStatusesByCode("03"));
                        }
                    }
                }
                order.setTransactionInfo(transactionInfo);
                orderRepository.save(order);
            }

            
            return ResponseUtil.getResponseEntity(order.getPaymentStatus().getCode(),order.getPaymentStatus().getDescription(),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}

//    private String configBody(String amount, String ipAddress,  String orderInfo, String orderReference) {
//        Map<String, Object> requestBodyMap = new HashMap<>();
//
//        requestBodyMap.put("access-code", accessCode);
//        requestBodyMap.put("amount", amount);
//        requestBodyMap.put("cancel_url", cancelUrl);
//        requestBodyMap.put("currency", "VND");
////        requestBodyMap.put("device", device);
//        requestBodyMap.put("ip_address", "192.168.1.108");
////        requestBodyMap.put("issuer_code", issuerCode);
//        requestBodyMap.put("merchant_id", merchantId);
//        requestBodyMap.put("order_info", "Thanh toan hoa don cho khach hang co so dien thoai " + orderInfo);
//        requestBodyMap.put("order_reference", orderReference);
//        requestBodyMap.put("pay_type", "pay");
//        requestBodyMap.put("return_url", returnUrl);
////        requestBodyMap.put("token", token);
////        requestBodyMap.put("merchant_user_reference", merchantUserReference);
////        requestBodyMap.put("customer_info", customerInfo);
////        requestBodyMap.put("bill_info", billInfo);
//        requestBodyMap.put("version", "2.0");
////        requestBodyMap.put("ipn_url", ipnUrl);
////        requestBodyMap.put("additional_data", additionalData);
//        requestBodyMap.put("language", "vi");
//        requestBodyMap.put("pp_version", "3.0");
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            String requestBodyJson = objectMapper.writeValueAsString(requestBodyMap);
//            return requestBodyJson;
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }