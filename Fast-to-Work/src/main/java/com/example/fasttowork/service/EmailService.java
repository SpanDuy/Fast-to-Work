package com.example.fasttowork.service;

import com.example.fasttowork.payload.request.EmailMessageRequest;

public interface EmailService {
    void sendSimpleMessage(EmailMessageRequest emailMessageRequest);
}
