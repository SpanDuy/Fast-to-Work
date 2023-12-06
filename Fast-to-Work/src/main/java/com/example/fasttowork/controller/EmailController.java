package com.example.fasttowork.controller;

import com.example.fasttowork.entity.UserEntity;
import com.example.fasttowork.payload.request.EmailMessageRequest;
import com.example.fasttowork.payload.request.JobVacancyRequest;
import com.example.fasttowork.payload.request.JobVacancySearchRequest;
import com.example.fasttowork.payload.request.ResumeRequest;
import com.example.fasttowork.payload.response.JobVacancyResponse;
import com.example.fasttowork.service.EmailService;
import com.example.fasttowork.service.UserService;
import com.example.fasttowork.service.serviceImpl.EmailServiceImpl;
import com.example.fasttowork.service.serviceImpl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/email/send")
    public String sendOfferMessage(@ModelAttribute("emailMessageRequest") EmailMessageRequest emailMessageRequest,
                                   HttpSession session,
                                   Model model) {

        EmailMessageRequest emailMessage = (EmailMessageRequest) session.getAttribute("emailMessageRequest");
        emailMessage.setText(emailMessageRequest.getText());

        emailService.sendSimpleMessage(emailMessage);

        return "redirect:/all";
    }
}
