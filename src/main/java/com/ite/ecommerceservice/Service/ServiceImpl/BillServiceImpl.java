package com.ite.ecommerceservice.Service.ServiceImpl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ite.ecommerceservice.Entity.Bill;
import com.ite.ecommerceservice.constants.StatusList;

import com.ite.ecommerceservice.Repository.BillRepository;
import com.ite.ecommerceservice.Security.AESUtil;
import com.ite.ecommerceservice.Service.BillService;
import com.ite.ecommerceservice.constants.MessageConstant;
import com.ite.ecommerceservice.utils.ResponseUtil;
import com.ite.ecommerceservice.Model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class BillServiceImpl implements BillService {
    @Autowired
    private BillRepository billRepository;
    private final ObjectMapper objectMapper;
    private final String apikey;
    @Autowired
    private StatusList statusList;

    public BillServiceImpl(ObjectMapper objectMapper, @Value("${api.key}") String apikey) {
        this.objectMapper = objectMapper;
        this.apikey = apikey;
    }


    @Override
    public ResponseEntity<?> getBillById(String orderId, HttpServletRequest request) throws Exception {
        String apikey1 = request.getHeader("apikey");
        System.out.println(apikey);
        String decryptedAPIKey = AESUtil.decrypt(apikey1);
        System.out.println(decryptedAPIKey);
        CustomResponse customResponse2 = new CustomResponse();
        CustomResponse3 customResponse3 = new CustomResponse3();
        if (apikey1 != null && apikey.equals(decryptedAPIKey)) {
            List<Bill> bill = billRepository.findBillByOrderId(orderId);
            List<BillDTOresponse> billDTOresponses = new ArrayList<>();
            for (Bill bills : bill) {
                BillDTOresponse billDTOresponse = Mapper.tobillDTOresponse(bills);
                if ("00".equals(billDTOresponse.getStatus()))  {
                    billDTOresponses.add(billDTOresponse);
                }
            }
            customResponse2 =CustomResponse.builder().code(ErrorCode.SUCCESSS.getCode()).message(ErrorCode.SUCCESSS.getDescription()).Bill(billDTOresponses).build();
            String json = objectMapper.writeValueAsString(customResponse2);
            String encryptedData = AESUtil.encrypt(json);
            return ResponseEntity.ok(encryptedData);
        } else {
            customResponse3 = CustomResponse3.builder().message(ErrorCode.ERROR_APIKEY.getDescription()).build();
            String json = objectMapper.writeValueAsString(customResponse3);
            String encryptedData = AESUtil.encrypt(json);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(encryptedData);
        }

    }


    @Override
    public ResponseEntity<?> getBill(HttpServletRequest request) throws Exception {
        String apikey1 = request.getHeader("apikey");
        System.out.println(apikey1);
        String decryptedAPIKey = AESUtil.decrypt(apikey1);
        System.out.println(decryptedAPIKey);
        CustomResponse customResponse = new CustomResponse();
        CustomResponse3 customResponse3 = new CustomResponse3();
        try {
            if (apikey1 != null && apikey.equals(decryptedAPIKey)) {
                String statusCode = "00";
                List<Bill> bills = billRepository.findBillBystatus(statusCode);
                List<BillDTOresponse> billDTOresponses = new ArrayList<>();
                for (Bill bill : bills) {
                    BillDTOresponse billDTOresponse = Mapper.tobillDTOresponse(bill);
                    billDTOresponses.add(billDTOresponse);
                }
                customResponse =CustomResponse.builder()
                        .code(ErrorCode.SUCCESSS.getCode())
                        .message(ErrorCode.SUCCESSS.getDescription())
                        .Bill(billDTOresponses).build();
                String json = objectMapper.writeValueAsString(customResponse);
                String encryptedData = AESUtil.encrypt(json);
                return ResponseEntity.ok(encryptedData);
            }

        } catch (Exception e){
            e.printStackTrace(); // Hoặc log lỗi ra để debug
        }

// Nếu điều kiện không được thỏa mãn hoặc có lỗi xảy ra, trả về phản hồi lỗi
        customResponse3 = CustomResponse3.builder()
                .message(ErrorCode.ERROR_APIKEY.getDescription())
                .build();
        String json = objectMapper.writeValueAsString(customResponse3);
        String encryptedData = AESUtil.encrypt(json);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(encryptedData);
    }

    @Override
    public ResponseEntity<?> searchBillss(String orderId, String email, int page, int size, HttpServletRequest request) throws Exception {
        String apikey1 = request.getHeader("apikey");
        CustomResponse customResponse2 = new CustomResponse();
        System.out.println(apikey1);
        String decryptAPIKey = AESUtil.decrypt(apikey1);
        System.out.println(decryptAPIKey);
        if (apikey1 != null && apikey.equals(decryptAPIKey)) {
            Pageable pageable = (Pageable) PageRequest.of(page, size, Sort.by("total_price").ascending());
            Page<Bill> bills;
            if (!"null".equals(orderId) || !"null".equals(email)){
                bills = billRepository.findByOrderIdOrEmail(orderId,email,pageable);
            }else{
                bills =billRepository.findAll(pageable);
            }
            List<BillDTOresponse> billDTOresponses = new ArrayList<>();
            for (Bill bill : bills) {
                BillDTOresponse billDTOresponse = Mapper.tobillDTOresponse(bill);
                if ("00".equals(billDTOresponse.getStatus())) {
                    billDTOresponses.add(billDTOresponse);}}
            long totalRecords = bills.getTotalElements();
            int currentPageSize = billDTOresponses.size();
            BillSearchResult roleGroupSearchResult = new BillSearchResult(totalRecords,currentPageSize,billDTOresponses);
            customResponse2 =CustomResponse.builder().code(ErrorCode.SUCCESSS.getCode()).message(ErrorCode.SUCCESSS.getDescription()).Bill(roleGroupSearchResult).build();
            String json = objectMapper.writeValueAsString(customResponse2);
            String encryptedData = AESUtil.encrypt(json);
            return ResponseEntity.status(HttpStatus.OK).body(encryptedData);
        } else {
            CustomResponse3 customResponse3 = new CustomResponse3();
            customResponse3 = CustomResponse3.builder().message(ErrorCode.ERROR_APIKEY.getDescription()).build();
            String json = objectMapper.writeValueAsString(customResponse3);
            String encryptedData = AESUtil.encrypt(json);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(encryptedData);
        }
    }


    @Override
    public ResponseEntity<?> getBillRefundById(String orderId, HttpServletRequest request) throws Exception {
        String apikey1 = request.getHeader("apikey");
        System.out.println(apikey);
        String decryptedAPIKey = AESUtil.decrypt(apikey1);
        System.out.println(decryptedAPIKey);
        CustomResponse2 customResponse = new CustomResponse2();
        if (apikey1 != null && apikey.equals(decryptedAPIKey)) {
            List<Bill> bill = billRepository.findBillByOrderId(orderId);
            List<BillDTOresponse> billDTOresponses = new ArrayList<>();
            for (Bill bills : bill) {
                BillDTOresponse billDTOresponse = Mapper.tobillDTOresponse(bills);
                if ("11".equals(billDTOresponse.getStatus())) {
                    billDTOresponses.add(billDTOresponse);
                }
            }
            customResponse =CustomResponse2.builder().code(ErrorCode.SUCCESSS.getCode()).message(ErrorCode.SUCCESSS.getDescription()).BillRefund(billDTOresponses).build();
            String json = objectMapper.writeValueAsString(customResponse);
            String encryptedData = AESUtil.encrypt(json);
            return ResponseEntity.ok(encryptedData);
        } else {
            CustomResponse3 customResponse3 = new CustomResponse3();
            customResponse3 = CustomResponse3.builder().message(ErrorCode.ERROR_APIKEY.getDescription()).build();
            String json = objectMapper.writeValueAsString(customResponse3);
            String encryptedData = AESUtil.encrypt(json);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(encryptedData);
        }

    }


    @Override
    public ResponseEntity<?> getBillRefund(HttpServletRequest request) throws Exception {
        String apikey1 = request.getHeader("apikey");
        System.out.println(apikey);
        String decryptedAPIKey = AESUtil.decrypt(apikey1);
        System.out.println(decryptedAPIKey);
        CustomResponse2 customResponse2 = new CustomResponse2();
        if (apikey1 != null && apikey.equals(decryptedAPIKey)) {
            String statuscode ="11";
            List<Bill> bills = billRepository.findBillBystatus(statuscode);

            customResponse2 =CustomResponse2.builder().code(ErrorCode.SUCCESSS.getCode()).message(ErrorCode.SUCCESSS.getDescription()).BillRefund(bills).build();
            String json = objectMapper.writeValueAsString(customResponse2);
            String encryptedData = AESUtil.encrypt(json);
            return ResponseEntity.ok(encryptedData);
        } else {
            CustomResponse3 customResponse3 = new CustomResponse3();
            customResponse3 = CustomResponse3.builder().message(ErrorCode.ERROR_APIKEY.getDescription()).build();
            String json = objectMapper.writeValueAsString(customResponse3);
            String encryptedData = AESUtil.encrypt(json);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(encryptedData);
        }

    }

    @Override
    public ResponseEntity<?> searchBillRefund(String orderId, String email, int page, int size, HttpServletRequest request) throws Exception {
        String apikey1 = request.getHeader("apikey"); // Lấy giá trị của API key từ header
        CustomResponse2 customResponse2 = new CustomResponse2();
        System.out.println(apikey1);
        String decryptAPIKey = AESUtil.decrypt(apikey1);
        System.out.println(decryptAPIKey);
        if (apikey1 != null && apikey.equals(decryptAPIKey)) {
            Pageable pageable = (Pageable) PageRequest.of(page, size, Sort.by("total_price").ascending());
            Page<Bill> bills;
            if (!"null".equals(orderId) || !"null".equals(email)){
                bills = billRepository.findByOrderIdOrEmail(orderId,email,pageable);
            }else{
                bills =billRepository.findAll(pageable);
            }
            List<BillDTOresponse> billDTOresponses = new ArrayList<>();
            for (Bill bill : bills) {
                BillDTOresponse billDTOresponse = Mapper.tobillDTOresponse(bill);
                if ("11".equals(billDTOresponse.getStatus())) {
                    billDTOresponses.add(billDTOresponse);}}
            long totalRecords = bills.getTotalElements();
            int currentPageSize = billDTOresponses.size();
            BillSearchResult roleGroupSearchResult = new BillSearchResult(totalRecords,currentPageSize,billDTOresponses);
            customResponse2 =CustomResponse2.builder().code(ErrorCode.SUCCESSS.getCode()).message(ErrorCode.SUCCESSS.getDescription()).BillRefund(roleGroupSearchResult).build();
            String json = objectMapper.writeValueAsString(customResponse2);
            String encryptedData = AESUtil.encrypt(json);
            return ResponseEntity.ok(encryptedData);
        } else {
            CustomResponse3 customResponse3 = new CustomResponse3();
            customResponse3 = CustomResponse3.builder().message(ErrorCode.ERROR_APIKEY.getDescription()).build();
            String json = objectMapper.writeValueAsString(customResponse3);
            String encryptedData = AESUtil.encrypt(json);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(encryptedData);
        }
    }


}



