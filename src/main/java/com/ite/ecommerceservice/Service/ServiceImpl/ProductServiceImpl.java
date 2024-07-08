package com.ite.ecommerceservice.Service.ServiceImpl;

import com.ite.ecommerceservice.Entity.Category;
import com.ite.ecommerceservice.Entity.Products;
import com.ite.ecommerceservice.Repository.CategoryRepository;
import com.ite.ecommerceservice.Repository.ProductRepository;
import com.ite.ecommerceservice.Service.ProductService;
import com.ite.ecommerceservice.config.SystemLogger;
import com.ite.ecommerceservice.constants.LogStepConstant;
import com.ite.ecommerceservice.constants.MessageConstant;
import com.ite.ecommerceservice.payload.dto.ProductDTO;
import com.ite.ecommerceservice.payload.request.SearchProductRequest;
import com.ite.ecommerceservice.payload.response.ProductsResponse;
import com.ite.ecommerceservice.utils.ResponseUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SystemLogger logger;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<?> getProducts(HttpServletRequest request,String categoryId,String subCategoryId,Long minPrice,Long maxPrice, Integer page, Integer size) {
        try {

            logger.log(Thread.currentThread().getName(), request.getHeader("requestId"),"Get list product", LogStepConstant.BEGIN_PROCESS,"");

            Pageable pageable = PageRequest.of(page, size, Sort.by("category_id").ascending());
            Page<Products> productPage = null;
            if(minPrice == null && maxPrice == null){
                if ((categoryId == null || categoryId.isEmpty()) && (subCategoryId == null || subCategoryId.isEmpty())) {
                    logger.log(Thread.currentThread().getName(), request.getHeader("requestId"),"Get list product", LogStepConstant.BEGIN_CALL_DATABASE,"Get all product");
                    productPage = productRepository.findAll(pageable);
                    logger.log(Thread.currentThread().getName(), request.getHeader("requestId"),"Get list product", LogStepConstant.END_CALL_DATABASE,"Get all product");

                }else {
                    if(subCategoryId == null && categoryId != null){
                        logger.log(Thread.currentThread().getName(), request.getHeader("requestId"),"Get list product", LogStepConstant.BEGIN_CALL_DATABASE,"Get all product by category");

                        Category category = categoryRepository.getCategoryById(categoryId);
                        List<String> ids = new ArrayList<>();
                        for(Category c : category.getChildren()){
                            ids.add(c.getId());
                        }
                        productPage = productRepository.findAllByCategoryIds(ids, pageable);
                        logger.log(Thread.currentThread().getName(), request.getHeader("requestId"),"Get list product", LogStepConstant.END_CALL_DATABASE,"Get all product by category");
                    }else {
                        logger.log(Thread.currentThread().getName(), request.getHeader("requestId"),"Get list product", LogStepConstant.BEGIN_CALL_DATABASE,"Get all product by category");
                        productPage = productRepository.getProductsByCategoryId(subCategoryId, pageable);
                        logger.log(Thread.currentThread().getName(), request.getHeader("requestId"),"Get list product", LogStepConstant.END_CALL_DATABASE,"Get all product by category");

                    }

                }
            }else {
                if(minPrice != null && maxPrice != null){
                    if ((categoryId == null || categoryId.isEmpty()) && (subCategoryId == null || subCategoryId.isEmpty())) {
                        logger.log(Thread.currentThread().getName(), request.getHeader("requestId"),"Get list product", LogStepConstant.BEGIN_CALL_DATABASE,"Get all product");
                        productPage = productRepository.findAllByPriceRange(minPrice,maxPrice,pageable);
                        logger.log(Thread.currentThread().getName(), request.getHeader("requestId"),"Get list product", LogStepConstant.END_CALL_DATABASE,"Get all product");

                    }else {
                        if(subCategoryId == null && categoryId != null){
                            logger.log(Thread.currentThread().getName(), request.getHeader("requestId"),"Get list product", LogStepConstant.BEGIN_CALL_DATABASE,"Get all product by category");

                            Category category = categoryRepository.getCategoryById(categoryId);
                            List<String> ids = new ArrayList<>();
                            for(Category c : category.getChildren()){
                                ids.add(c.getId());
                            }
                            productPage = productRepository.findAllByCategoryIdsAndPriceRange(ids,minPrice,maxPrice, pageable);
                            logger.log(Thread.currentThread().getName(), request.getHeader("requestId"),"Get list product", LogStepConstant.END_CALL_DATABASE,"Get all product by category");
                        }else {
                            logger.log(Thread.currentThread().getName(), request.getHeader("requestId"),"Get list product", LogStepConstant.BEGIN_CALL_DATABASE,"Get all product by category");
                            productPage = productRepository.findAllByCategoryIdAndPriceRange(subCategoryId,minPrice,maxPrice, pageable);
                            logger.log(Thread.currentThread().getName(), request.getHeader("requestId"),"Get list product", LogStepConstant.END_CALL_DATABASE,"Get all product by category");

                        }

                    }
                }else {
                    if (minPrice != null && maxPrice == null){
                        if ((categoryId == null || categoryId.isEmpty()) && (subCategoryId == null || subCategoryId.isEmpty())) {
                            logger.log(Thread.currentThread().getName(), request.getHeader("requestId"),"Get list product", LogStepConstant.BEGIN_CALL_DATABASE,"Get all product");
                            productPage = productRepository.findAllByMinPrice(minPrice,pageable);
                            logger.log(Thread.currentThread().getName(), request.getHeader("requestId"),"Get list product", LogStepConstant.END_CALL_DATABASE,"Get all product");

                        }else {
                            if(subCategoryId == null && categoryId != null){
                                logger.log(Thread.currentThread().getName(), request.getHeader("requestId"),"Get list product", LogStepConstant.BEGIN_CALL_DATABASE,"Get all product by category");

                                Category category = categoryRepository.getCategoryById(categoryId);
                                List<String> ids = new ArrayList<>();
                                for(Category c : category.getChildren()){
                                    ids.add(c.getId());
                                }
                                productPage = productRepository.findAllByCategoryIdsAndMinPrice(ids,minPrice, pageable);
                                logger.log(Thread.currentThread().getName(), request.getHeader("requestId"),"Get list product", LogStepConstant.END_CALL_DATABASE,"Get all product by category");
                            }else {
                                logger.log(Thread.currentThread().getName(), request.getHeader("requestId"),"Get list product", LogStepConstant.BEGIN_CALL_DATABASE,"Get all product by category");
                                productPage = productRepository.findAllByCategoryIdAndMinPrice(subCategoryId,minPrice, pageable);
                                logger.log(Thread.currentThread().getName(), request.getHeader("requestId"),"Get list product", LogStepConstant.END_CALL_DATABASE,"Get all product by category");

                            }

                        }
                    }
                }
            }
            
            

            List<ProductDTO> productDTOS = productPage.getContent().stream()
                    .map(product -> modelMapper.map(product, ProductDTO.class))
                    .collect(Collectors.toList());

            long totalRecords = productPage.getTotalElements();
            int currentPageSize = productDTOS.size();
            ProductsResponse productsResponse = ProductsResponse.builder()
                    .products(productDTOS)
                    .currentPageSize(currentPageSize)
                    .totalRecords(totalRecords)
                    .build();

            logger.log(Thread.currentThread().getName(), request.getHeader("requestId"),"Get list product", LogStepConstant.END_PROCESS,MessageConstant.SUCCESSFULLY);
            return ResponseEntity.ok().body(productsResponse);
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Thread.currentThread().getName(), request.getHeader("requestId"),"Get list product", LogStepConstant.END_PROCESS,"ERROR: " + e.getMessage());
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getProductDetail(String productId, HttpServletRequest request) {
        try{
            logger.log(Thread.currentThread().getName(), request.getHeader("requestId"),"Get product detail", LogStepConstant.BEGIN_PROCESS,productId);

            if(!productRepository.existsById(productId)){
                logger.log(Thread.currentThread().getName(), request.getHeader("requestId"),"Get product detail", LogStepConstant.END_PROCESS,MessageConstant.PRODUCT_NOT_EXIST);
                return ResponseUtil.getResponseEntity(MessageConstant.PRODUCT_NOT_EXIST, HttpStatus.BAD_REQUEST);
            }
            logger.log(Thread.currentThread().getName(), request.getHeader("requestId"),"Get product detail", LogStepConstant.BEGIN_CALL_DATABASE,productId);
            ProductDTO productDTO = modelMapper.map(productRepository.findById(productId).get(),ProductDTO.class);
            logger.log(Thread.currentThread().getName(), request.getHeader("requestId"),"Get product detail", LogStepConstant.END_CALL_DATABASE,productId);

            logger.log(Thread.currentThread().getName(), request.getHeader("requestId"),"Get product detail", LogStepConstant.END_PROCESS,MessageConstant.SUCCESSFULLY);
            return ResponseEntity.ok(productDTO);
        }catch (Exception e){
            e.printStackTrace();
            logger.log(Thread.currentThread().getName(), request.getHeader("requestId"),"Get product detail", LogStepConstant.END_PROCESS,"ERROR: " + e.getMessage());
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<?> search(SearchProductRequest searchProductRequest, HttpServletRequest request) {
        try{
            Pageable pageable = PageRequest.of(searchProductRequest.getPage(), searchProductRequest.getSize(), Sort.by("category_id").ascending());
            Page<Products> productsPage = productRepository.findByBrandOrName(searchProductRequest.getKeyWord(), pageable);

            List<ProductDTO> productDTOS = productsPage.getContent().stream()
                    .map(product -> modelMapper.map(product, ProductDTO.class))
                    .collect(Collectors.toList());

            long totalRecords = productsPage.getTotalElements();
            int currentPageSize = productDTOS.size();
            ProductsResponse productsResponse = ProductsResponse.builder()
                    .products(productDTOS)
                    .currentPageSize(currentPageSize)
                    .totalRecords(totalRecords)
                    .build();
            return ResponseEntity.ok().body(productsResponse);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getProductByFilter(HttpServletRequest request, long priceStart, long priceEnd) {
        try{

        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }
}
