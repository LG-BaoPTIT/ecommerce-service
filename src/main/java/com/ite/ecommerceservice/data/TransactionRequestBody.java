package com.ite.ecommerceservice.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestBody {
    private String access_code;
    private String merchant_id;
    private String order_reference;
   // private String transaction_number;
    private String pay_time;
    private String version;

    @Override
    public String toString() {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");
        jsonBuilder.append("\"access_code\":\"").append(access_code).append("\",");
        jsonBuilder.append("\"merchant_id\":\"").append(merchant_id).append("\",");
        jsonBuilder.append("\"order_reference\":\"").append(order_reference).append("\",");
        //jsonBuilder.append("\"transaction_number\":\"").append(transaction_number).append("\",");
        jsonBuilder.append("\"pay_time\":\"").append(pay_time).append("\",");
        jsonBuilder.append("\"version\":\"").append(version).append("\"");
        jsonBuilder.append("}");
        return jsonBuilder.toString();
    }
}
