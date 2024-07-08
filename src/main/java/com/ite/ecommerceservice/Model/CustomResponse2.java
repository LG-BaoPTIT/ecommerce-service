package com.ite.ecommerceservice.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class CustomResponse2 extends ResponseType2{
//    private BillDTOresponse billDTOresponse;
    @Override
    public String toString() {
        return "CustomResponse{" +
                "code='" + super.getCode() + '\'' +
                ", message='" + super.getMessage() + '\'' +
                '}';
    }
}
