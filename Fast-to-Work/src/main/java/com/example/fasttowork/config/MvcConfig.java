package com.example.fasttowork.config;

import com.example.fasttowork.entity.converter.SkillConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private SkillConverter skillConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(skillConverter);
    }
}
