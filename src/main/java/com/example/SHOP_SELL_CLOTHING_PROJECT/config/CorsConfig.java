package com.example.SHOP_SELL_CLOTHING_PROJECT.config;

/**
 * Project: SHOP_SELL_CLOTHING_PROJECT
 * Date: 2025/03/21
 * Time: 10:47 AM
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ 2025. All rights reserved
 */


@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Allow all APIs
                        .allowedOrigins("*") // Accept requests from all sources
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow all HTTP methods
                        .allowedHeaders("*") // Allow all headers
                        .allowCredentials(false); // If no cookie/token is needed
            }
        };
    }
}
