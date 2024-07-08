package com.ite.ecommerceservice.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestBody {
    @JsonProperty("access_code")
    private String access_code;

    private String amount;

    @JsonProperty("cancel_url")
    private String cancel_url;

    private String currency;

    @JsonProperty("ip_address")
    private String ip_address;

    @JsonProperty("merchant_id")
    private String merchant_id;

    @JsonProperty("order_info")
    private String order_info;

    @JsonProperty("order_reference")
    private String order_reference;

    @JsonProperty("pay_type")
    private String pay_type;

    @JsonProperty("return_url")
    private String return_url;

    private String version;

    private String ipn_url;

    private String language;

    @JsonProperty("pp_version")
    private String pp_version;

    @Override
    public String toString() {
        return "{\"access_code\":\"" + access_code + "\"," +
                "\"amount\":\"" + amount + "\"," +
                "\"currency\":\"" + currency + "\"," +
                "\"cancel_url\":\"" + cancel_url + "\"," +
                "\"ip_address\":\"" + ip_address + "\"," +
                "\"merchant_id\":\"" + merchant_id + "\"," +
                "\"order_info\":\"" + order_info + "\"," +
                "\"order_reference\":\"" + order_reference + "\"," +
                "\"pay_type\":\"" + pay_type + "\"," +
                "\"return_url\":\"" + return_url + "\"," +
                "\"version\":\"" + version + "\"," +
                "\"ipn_url\":\"" + ipn_url + "\"," +
                "\"language\":\"" + language + "\"," +
                "\"pp_version\":\"" + pp_version + "\"}";
    }


}
