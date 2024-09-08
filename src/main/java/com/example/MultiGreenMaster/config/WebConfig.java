package com.example.Flower.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedOrigins("http://localhost:3000") // 허용할 출처 : 특정 도메인만 받을 수 있음
                .allowedMethods("GET", "POST", "UPDATE", "DELETE") // 허용할 HTTP method
                .allowCredentials(true); // 쿠키 인증 요청 허용
    }
}
