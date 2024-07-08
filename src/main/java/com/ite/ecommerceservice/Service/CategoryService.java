package com.ite.ecommerceservice.Service;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CategoryService {
    ResponseEntity<?> getListCategory(HttpServletRequest request);
}
