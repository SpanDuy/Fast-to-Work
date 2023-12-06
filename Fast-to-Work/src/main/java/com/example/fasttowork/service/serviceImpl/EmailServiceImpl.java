package com.example.fasttowork.service.serviceImpl;

import com.example.fasttowork.payload.request.EmailMessageRequest;
import com.example.fasttowork.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender emailSender;

    @Override
    @Async
    public void sendSimpleMessage(EmailMessageRequest emailMessageRequest) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("fasttoworksender@gmail.com");
        message.setTo(emailMessageRequest.getEmailAddress());
        message.setSubject(emailMessageRequest.getSubject());
        message.setText(emailMessageRequest.getText());

        emailSender.send(message);
    }
}
