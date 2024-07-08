package com.ite.ecommerceservice.Model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillSearchResult {
    private long totalRecords;
    private int currentPageSize;
    private List<BillDTOresponse> roles;
}
