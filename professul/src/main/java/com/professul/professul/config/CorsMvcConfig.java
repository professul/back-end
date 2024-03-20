package com.professul.professul.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class CorsMvcConfig{
    @Bean
    public CorsFilter corsFilter(){
        CorsConfiguration configuration= new CorsConfiguration();
        configuration.setAllowCredentials(true); //내 서버의 json응답을 자바스크립트가 처리할 수 있게 하는 것
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:8080"));
        configuration.addExposedHeader("Authorization");
        configuration.addExposedHeader("access");
        configuration.addExposedHeader("refresh");
        configuration.setAllowedMethods(List.of("POST", "GET", "PUT", "DELETE", "PATCH"));
        configuration.addAllowedHeader("*"); //모든 header에 응답을 허용
        UrlBasedCorsConfigurationSource source= new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
    }

}