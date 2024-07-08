package com.ite.ecommerceservice.data;

import com.ite.ecommerceservice.Entity.Category;
import com.ite.ecommerceservice.Entity.OrderStatus;
import com.ite.ecommerceservice.Entity.PaymentStatus;
import com.ite.ecommerceservice.Repository.CategoryRepository;
import com.ite.ecommerceservice.Repository.OrderStatusRepository;
import com.ite.ecommerceservice.Repository.PaymentStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Autowired
    private PaymentStatusRepository paymentStatusRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        // Tạo và chèn dữ liệu mẫu
        initOrderStatuses();
    }

    private void initOrderStatuses() {
        // Tạo dữ liệu mẫu
        Category category = new Category();
        category.setId("3");
        category.setCategory_level(1);
        category.setName("Laptop");
        category.setDescription("Test");

        Category category2 = new Category();
        category2.setId("301");
        category2.setName("MAC");
        category2.setCategory_level(2);
        category2.setDescription("Test");

        Category category3 = new Category();
        category3.setId("302");
        category3.setCategory_level(2);

        category3.setName("ASUS");
        category3.setDescription("Test");

        Category category4 = new Category();
        category4.setId("303");
        category4.setCategory_level(2);

        category4.setName("Dell");
        category4.setDescription("Test");

        List<Category> subCategoryList = new ArrayList<>();
        subCategoryList.add(category2);
        subCategoryList.add(category3);
        subCategoryList.add(category4);
        category.setChildren(subCategoryList);

        categoryRepository.save(category);
//        PaymentStatus os1 = new PaymentStatus();
//        os1.setCode("00");
//        os1.setDescription("Đơn hàng thanh toán thành công");
//
//        PaymentStatus os2 = new PaymentStatus();
//        os2.setCode("01");
//        os2.setDescription("Đơn hàng chưa thanh toán");
//
//        PaymentStatus os3 = new PaymentStatus();
//        os3.setCode("02");
//        os3.setDescription("Đơn hàng đang trong quá trình thanh toán");
//
//        PaymentStatus os4 = new PaymentStatus();
//        os4.setCode("03");
//        os4.setDescription("Đơn hàng thanh toán thất bại");


//        paymentStatusRepository.save(os1);
//        paymentStatusRepository.save(os2);
//        paymentStatusRepository.save(os3);
//        paymentStatusRepository.save(os4);


        // Chèn dữ liệu mẫu vào MongoDB
        // Đảm bảo xóa dữ liệu cũ trước khi chèn để tránh lỗi do trùng lặp unique index
//        orderStatusRepository.deleteAll();
//        orderStatusRepository.save(os1);
//        orderStatusRepository.save(os2);

    }
}
