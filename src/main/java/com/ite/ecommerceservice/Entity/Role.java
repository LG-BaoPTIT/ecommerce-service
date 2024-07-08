package com.ite.ecommerceservice.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Document(collection = "Roles")
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    private String id;
    private String roleCode;
    private String roleName;
    private LocalDateTime createdAt;

}
