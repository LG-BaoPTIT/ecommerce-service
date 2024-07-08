package com.ite.ecommerceservice.Service;


import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface BillService {


//    public String updateAccountStatus(userDTo userDto);

//    public void updateAccountStatus(String username, String newStatus);

//    public ResponseEntity<?> updateAccountStatus(userDTo userDto);
//
//    void run(String... args) throws Exception;

//    Page<Role> searchRoleGroups(String roleCode, String roleName, LocalDateTime createdAt, Pageable pageable);

   public ResponseEntity<?> getBillById(String orderId, HttpServletRequest request) throws Exception;



    public ResponseEntity<?> getBill(HttpServletRequest request) throws Exception;

 public ResponseEntity<?> searchBillss(String orderId, String email, int page, int size, HttpServletRequest request) throws Exception;
    public ResponseEntity<?> getBillRefundById(String orderId, HttpServletRequest request) throws Exception;



    public ResponseEntity<?> getBillRefund(HttpServletRequest request) throws Exception;

 public ResponseEntity<?> searchBillRefund(String orderId, String email, int page, int size, HttpServletRequest request) throws Exception;




//    public ResponseEntity<?> createOrder(OrderRequest orderRequest);
}
