package com.ite.ecommerceservice.Controller;

import com.ite.ecommerceservice.Service.CategoryService;
import com.ite.ecommerceservice.constants.MessageConstant;
import com.ite.ecommerceservice.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/ecommerce/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<?> getListCategory(HttpServletRequest request){
        try{
            return categoryService.getListCategory(request);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
