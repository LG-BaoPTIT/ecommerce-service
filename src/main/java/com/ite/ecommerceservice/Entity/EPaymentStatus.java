package com.ite.ecommerceservice.Entity;

public enum EPaymentStatus {
    UNPAID("Đơn hàng chưa thanh toán"),
    PAYMENT_IN_PROGRESS("Đơn hàng đang trong quá trình thanh toán"),
    PAYMENT_SUCCESSFUL("Đơn hàng thanh toán thành công"),
    //PAYMENT_TIME_EXPIRES(""),
    PAYMENT_FAILED("Đơn hàng thanh toán thất bại");
    private final String description;

    EPaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
