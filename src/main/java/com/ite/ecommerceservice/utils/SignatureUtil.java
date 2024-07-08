package com.ite.ecommerceservice.utils;

import com.ite.ecommerceservice.cachedrequest.CachedBodyHttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class SignatureUtil {
    @Value("${api.key}")
    private String key;
    public boolean verifySignature(HttpServletRequest request){
        try {
            CachedBodyHttpServletRequest cachedBodyHttpServletRequest = new CachedBodyHttpServletRequest(request);

            // Get the signature from the header
            String signature = cachedBodyHttpServletRequest.getHeader("Signature");
            String body = getRequestBody(cachedBodyHttpServletRequest);
//            String accessToken = null;
//            String authorizationHeader = request.getHeader("Authorization");
//
//            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//                accessToken = authorizationHeader.substring(7);
//            }
////            String header = extractHeaders(request);
////            String method = request.getMethod();
////            String uri = request.getRequestURI();
////            String payload = extractPayload(request);
            String receivedSignature = request.getHeader("Signature");
            String calculatedSignature = signRequest(body);
            System.out.println(calculatedSignature);
            return receivedSignature != null && receivedSignature.equals(calculatedSignature);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String signRequest(String payload) throws NoSuchAlgorithmException, InvalidKeyException {
        String requestData = payload;
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);
        byte[] signatureBytes = mac.doFinal(requestData.getBytes());
        return Base64.getEncoder().encodeToString(signatureBytes);
    }
    private String getRequestBody(HttpServletRequest request) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        try (ServletInputStream inputStream = request.getInputStream()) {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
        }
        return byteArrayOutputStream.toString(StandardCharsets.UTF_8.name());
    }
}
