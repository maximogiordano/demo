package com.example.demo.config;

import feign.okhttp.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();  // required for PATCH with FeignClient
    }
}
