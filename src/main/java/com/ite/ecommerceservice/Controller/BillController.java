package com.ite.ecommerceservice.Controller;


import com.ite.ecommerceservice.Service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Transactional
@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class BillController {
    @Autowired
    private BillService billService;
    @GetMapping("/account/bill/billDetail")
    public ResponseEntity<?> getBillById(@RequestParam(name = "orderId") String orderId, HttpServletRequest request
                                         ) throws Exception {
        return billService.getBillById(orderId,request);
    }
    @GetMapping("/account/bill")
    public ResponseEntity<?> getBill( HttpServletRequest request) throws Exception {
        return billService.getBill(request);
    }

    @GetMapping("/account/bill/search")
    public ResponseEntity<?> searchBillss(
            @RequestParam(required = false) String orderId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) int page,
            @RequestParam(required = false) int size
            , HttpServletRequest request) throws Exception {
//        List<Bill> bills = accountService.searchBills(orderId, name, product);
        return  billService.searchBillss(orderId,email,page,size,request);
    }@GetMapping("/account/billRefund/billDetail")
    public ResponseEntity<?> getBillRefundById(@RequestParam(name = "orderId") String orderId, HttpServletRequest request
                                         ) throws Exception {
        return billService.getBillRefundById(orderId,request);
    }
    @GetMapping("/account/billRefund")
    public ResponseEntity<?> getBillRefund( HttpServletRequest request) throws Exception {
        return billService.getBillRefund(request);
    }

    @GetMapping("/account/billRefund/search")
    public ResponseEntity<?> searchBillRefund(
            @RequestParam(required = false) String orderId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) int page,
            @RequestParam(required = false) int size
            , HttpServletRequest request) throws Exception {
//        List<Bill> bills = accountService.searchBills(orderId, name, product);
        return  billService.searchBillRefund(orderId,email,page,size,request);

}}
