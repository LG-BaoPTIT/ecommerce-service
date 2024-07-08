package com.ite.ecommerceservice.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class CustomResponse3 extends ResponseType3 {

    @Override
    public String toString() {
        return "CustomResponse{" +
                ", message='" + super.getMessage() + '\'' +
                '}';
    }

}
