package com.bujdi.carRecords.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{

    @Autowired
    private RequestLoggingInterceptor requestLoggingInterceptor;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NotNull CorsRegistry registry) {
                registry.addMapping("/**")  // Enable CORS for all paths
                        .allowedOriginPatterns("*")  // Allow requests from any origin
                        .allowedMethods("*")  // Allow all HTTP methods
                        .allowedHeaders("*") // Allow all headers
                        .allowCredentials(true);

            }
        };
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Register the RequestLoggingInterceptor
        registry.addInterceptor(requestLoggingInterceptor);
    }
}