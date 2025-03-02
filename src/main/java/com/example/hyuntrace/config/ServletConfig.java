package com.example.hyuntrace.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class ServletConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:static/js/");
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:static/assets/");
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:static/css/");

    }
}
