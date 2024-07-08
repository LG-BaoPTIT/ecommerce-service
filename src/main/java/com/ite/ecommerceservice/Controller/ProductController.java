package com.ite.ecommerceservice.Controller;

import com.ite.ecommerceservice.Entity.Products;
import com.ite.ecommerceservice.Service.ProductService;
import com.ite.ecommerceservice.constants.MessageConstant;
import com.ite.ecommerceservice.payload.request.SearchProductRequest;
import com.ite.ecommerceservice.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/ecommerce/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("")
    public ResponseEntity<?> getProducts(HttpServletRequest request,
                                         @RequestParam(name = "categoryId", required = false) String categoryId,
                                         @RequestParam(name = "subCategoryId",required = false ) String subCategoryId,
                                         @RequestParam(name = "page", defaultValue = "0") Integer page,
                                         @RequestParam(name = "size", defaultValue = "10") Integer size,
                                         @RequestParam(name = "minPrice", required = false) Long minPrice,
                                         @RequestParam(name = "maxPrice", required = false) Long maxPrice
                                         ){
        try{
            return productService.getProducts(request,categoryId,subCategoryId,minPrice,maxPrice,page,size);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<?> getProductByFilter(HttpServletRequest request,@RequestParam("priceStart") long priceStart, @RequestParam("priceEnd") long priceEnd ){
        return productService.getProductByFilter(request,priceStart,priceEnd);
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getProductDetail(@RequestParam("productId") String productId,HttpServletRequest request){
        try{
            return productService.getProductDetail(productId,request);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchProduct(@RequestBody SearchProductRequest searchProductRequest,HttpServletRequest request){
        try{
            return productService.search(searchProductRequest, request);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
