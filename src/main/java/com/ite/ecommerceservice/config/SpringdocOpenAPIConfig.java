package com.ite.ecommerceservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.SpringDocConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SpringdocOpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ecommerce service")
                        .version("1.0")
                        .description("API documentation for ecommerce service")
                        .contact(new Contact()
                                .name("LGB")
                                .email("bao2002pytn@gmail.com")
                                .url("https://example.com/contact"))
                        .license(new License()
                                .name("License")
                                .url("https://example.com/license")));
    }
    @Bean
    public SpringDocConfigProperties springDocConfigProperties() {
        return new SpringDocConfigProperties();
    }


}