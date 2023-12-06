package com.example.fasttowork.config;

import com.example.fasttowork.entity.converter.SkillConverter;
import com.example.fasttowork.validator.JobVacancyValidator;
import com.example.fasttowork.validator.ResumeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.validation.Validator;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Properties;

@Configuration
@EnableAsync
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private SkillConverter skillConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(skillConverter);
    }

    @Bean
    public Validator validatorJobVacancy() {
        return new JobVacancyValidator();
    }

    @Bean
    public Validator validatorResumeValidator() {
        return new ResumeValidator();
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("fasttoworksender@gmail.com");
        mailSender.setPassword("lkftgctboqcankda");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }
}
