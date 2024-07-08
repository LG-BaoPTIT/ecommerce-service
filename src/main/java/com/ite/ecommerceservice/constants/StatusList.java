package com.ite.ecommerceservice.constants;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
@Setter
@Getter

@Builder
@AllArgsConstructor
@ToString
@Component
public class StatusList {
    private Map<String, String> statusList;

    public StatusList() {
        // Khởi tạo danh sách trạng thái
        this.statusList = new HashMap<>();
        this.statusList.put("00", "Thành công");
        this.statusList.put("01", "Chờ thanh toán");
        this.statusList.put("02", "Hủy giao dịch");
        this.statusList.put("03", "Không thành công");
        this.statusList.put("04", "Chờ xác thực OTP");
        this.statusList.put("05", "OTP không đúng");
        this.statusList.put("06", "Chờ xác thực");
        this.statusList.put("07", "Bị chặn");
        this.statusList.put("10", "Khởi tạo giao dịch");
        this.statusList.put("11", "Hoàn tiền");
        this.statusList.put("19", "Khác");
    }

    public Map<String, String> getStatusList() {
        return statusList;
    }

    public void setStatusList(Map<String, String> statusList) {
        this.statusList = statusList;
    }
}
