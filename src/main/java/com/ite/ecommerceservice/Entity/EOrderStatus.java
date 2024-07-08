package com.ite.ecommerceservice.Entity;

public enum EOrderStatus {
    ORDER_CREATED("Đơn hàng đã được tạo"),
    ORDER_CANCELLED("Đơn hàng đã bị hủy");

    private final String description;

    EOrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
//    UNPAID,       // Đơn hàng đã được tạo nhưng chưa thanh toán
//    PAID,         // Đơn hàng đã thanh toán
//    CANCELLED;    // Đơn hàng đã bị hủy
////    CONFIRMED,    // Đơn hàng đã được xác nhận
////    SHIPPED,      // Đơn hàng đã được gửi đi
////    DELIVERED,    // Đơn hàng đã được giao

}
