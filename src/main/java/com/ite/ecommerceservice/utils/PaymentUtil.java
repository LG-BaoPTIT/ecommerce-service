package com.ite.ecommerceservice.utils;

import com.google.common.hash.Hashing;
import com.ite.ecommerceservice.data.PaymentRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.List;

@Service
public class PaymentUtil {
    @Autowired
    private RequestIdGenerator requestIdGenerator;

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

    public HttpHeaders configHeader(String body){

        String digest =  calculateDigest(body);
        String dateTime =  Long.toString(Instant.now().toEpochMilli());
        String dataHash = generateDataString(dateTime, accessCode, digest);
        String signature = createSignature(dataHash,hashSecret);
        HttpHeaders headers = new HttpHeaders();
        headers.set("date-time", dateTime);
        headers.set("access-code", accessCode);
        headers.set("digest", digest);
        headers.set("hash-field", "date-time, access-code, digest");
        headers.set("request-id", requestIdGenerator.generateRequestId());
        headers.set("signature","type=HMACSHA256, value="+ signature);

        headers.set("content-type", "application/json");
        return headers;
    }

    public HttpHeaders configHeader(HttpServletRequest request, String body){

        String digest =  calculateDigest(body);
        String dateTime =  Long.toString(Instant.now().toEpochMilli());
        String dataHash = generateDataString(dateTime, accessCode, digest);
        String signature = createSignature(dataHash,hashSecret);
        HttpHeaders headers = new HttpHeaders();
        headers.set("date-time", dateTime);
        headers.set("access-code", accessCode);
        headers.set("digest", digest);
        headers.set("hash-field", "date-time, access-code, digest");
        headers.set("request-id", request.getHeader("requestId"));
        headers.set("signature","type=HMACSHA256, value="+ signature);

        headers.set("content-type", "application/json");
        return headers;
    }

    public PaymentRequestBody configBody(String amount, String ipAddress, String orderInfo, String orderReference) {
        PaymentRequestBody requestBody = new PaymentRequestBody();
        requestBody.setAccess_code(accessCode);
        requestBody.setAmount(amount);
        requestBody.setCancel_url(cancelUrl + "?orderId=" + orderReference);
        requestBody.setCurrency("VND");
        requestBody.setIp_address("192.168.1.108");
        requestBody.setMerchant_id(merchantId);
        requestBody.setOrder_info("Thanh toan hoa don cho khach hang co so dien thoai " + orderInfo);
        requestBody.setOrder_reference(orderReference);
        requestBody.setPay_type("pay");
        requestBody.setReturn_url(returnUrl + "?orderId=" + orderReference);
        requestBody.setVersion("2.0");
        requestBody.setIpn_url(ipnUrl);
        requestBody.setLanguage("vi");
        requestBody.setPp_version("3.0");

        return requestBody;
    }
    public String generateDataString(String dateTime, String accessCode, String digest) {
        return "date-time=" + dateTime + "&access-code=" + accessCode + "&digest=" + digest;
    }

    public String calculateDigest(String data) {
        byte[] encodedBytes = Base64.getEncoder().encode(data.getBytes());
        return new String(encodedBytes);
    }

    public String createSignature(String data, String key) {
        try {
            byte[] keyBytes = key.getBytes();

            Mac hmacSha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
            hmacSha256.init(secretKey);

            return Hashing.hmacSha256(keyBytes).hashString(data, StandardCharsets.UTF_8).toString();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean checkSignature(ResponseEntity<String> responseEntity) {
        if (responseEntity == null) {
            return false;
        }

        HttpHeaders headers = responseEntity.getHeaders();
        String body = responseEntity.getBody();
        List<String> hashFieldList = headers.get("hash-field");

        if (hashFieldList == null || hashFieldList.isEmpty()) {
            return false;
        }

        String fieldsString = hashFieldList.get(0);
        String[] hashField = fieldsString.split(",");

        StringBuilder dataHash = new StringBuilder();
        String digest = calculateDigest(body);
        for (int i = 0; i < hashField.length; i++) {
            String field = hashField[i].trim(); // Trim để loại bỏ khoảng trắng thừa
            List<String> valueList = headers.get(field);
            String value = (valueList != null && !valueList.isEmpty()) ? valueList.get(0) : "null";

            dataHash.append(field).append("=");

            if (field.equals(digest)) {
                dataHash.append(digest);
            } else {
                dataHash.append(value);
            }

            if (i < hashField.length - 1) {
                dataHash.append("&");
            }
        }

        String s = dataHash.toString();
        String calculatorSignature ="type=HMACSHA256, value="+ createSignature(dataHash.toString(),hashSecret);
        String receiveSignature = headers.get("signature").get(0);
        return calculatorSignature.equals(receiveSignature);
    }
}
