package com.studywith.api.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/uploads/profile/**")
                .addResourceLocations("file:/C:/ghpj/study_web/uploads/profile/");
        registry
                .addResourceHandler("/uploads/thumbnail/**")
                .addResourceLocations("file:/C:/ghpj/study_web/uploads/thumbnail/");
    }

}
