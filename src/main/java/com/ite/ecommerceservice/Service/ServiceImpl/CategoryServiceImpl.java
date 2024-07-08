package com.ite.ecommerceservice.Service.ServiceImpl;

import com.ite.ecommerceservice.Entity.Category;
import com.ite.ecommerceservice.Repository.CategoryRepository;
import com.ite.ecommerceservice.Service.CategoryService;
import com.ite.ecommerceservice.config.SystemLogger;
import com.ite.ecommerceservice.constants.LogStepConstant;
import com.ite.ecommerceservice.constants.MessageConstant;
import com.ite.ecommerceservice.payload.dto.CategoryDTO;
import com.ite.ecommerceservice.utils.ResponseUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SystemLogger logger;

    @Override
    public ResponseEntity<?> getListCategory(HttpServletRequest request) {
        try{
            logger.log(Thread.currentThread().getName(), request.getHeader("requestId"),"Get list category", LogStepConstant.BEGIN_PROCESS,"");

            logger.log(Thread.currentThread().getName(), request.getHeader("requestId"),"Get list category", LogStepConstant.BEGIN_CALL_DATABASE,"");
            List<CategoryDTO> categories = categoryRepository.findAll().stream().map(category -> modelMapper.map(category,CategoryDTO.class)).collect(Collectors.toList());
            logger.log(Thread.currentThread().getName(), request.getHeader("requestId"),"Get list category", LogStepConstant.END_CALL_DATABASE,"");

            logger.log(Thread.currentThread().getName(), request.getHeader("requestId"),"Get list category", LogStepConstant.END_PROCESS,"");
            return ResponseEntity.ok(categories);
        }catch (Exception e){
            e.printStackTrace();
            logger.log(Thread.currentThread().getName(), request.getHeader("requestId"),"Get list category", LogStepConstant.END_PROCESS,"ERROR: "+e.getMessage());
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
