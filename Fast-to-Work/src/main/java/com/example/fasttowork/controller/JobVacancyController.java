package com.example.fasttowork.controller;

import com.example.fasttowork.entity.JobVacancy;
import com.example.fasttowork.entity.Skill;
import com.example.fasttowork.entity.converter.SkillConverter;
import com.example.fasttowork.payload.request.EmailMessageRequest;
import com.example.fasttowork.payload.request.JobVacancyRequest;
import com.example.fasttowork.payload.request.JobVacancySearchRequest;
import com.example.fasttowork.payload.response.JobVacancyResponse;
import com.example.fasttowork.payload.response.ResumeResponse;
import com.example.fasttowork.security.SecurityUtil;
import com.example.fasttowork.service.EmailService;
import com.example.fasttowork.service.JobVacancyService;
import com.example.fasttowork.service.UserService;
import com.example.fasttowork.validator.JobVacancyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class JobVacancyController {
    private final JobVacancyService jobVacancyService;
    private final UserService userService;

    @GetMapping("/job-vacancy/all")
    public String getAllJobVacancy(Model model,
                                   HttpServletResponse response) {
        JobVacancySearchRequest jobVacancySearchRequest = new JobVacancySearchRequest();
        List<JobVacancy> jobVacancies = jobVacancyService.getAllJobVacancy();

        response.setHeader("Cache-Control", "public, max-age=3600");

        model.addAttribute("jobVacancySearchRequest", jobVacancySearchRequest);
        model.addAttribute("jobVacancies", jobVacancies);
        return "job-vacancy-all";
    }

    @PostMapping("/job-vacancy/all")
    public String getAllJobVacancy(@ModelAttribute("resume") JobVacancySearchRequest jobVacancySearchRequest,
                                   BindingResult result,
                                   Model model,
                                   HttpServletResponse response) {
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            model.addAttribute("jobVacancySearchRequest", jobVacancySearchRequest);
            return "job-vacancy-all";
        }

        List<JobVacancy> jobVacancies = jobVacancyService.searchJobVacancy(jobVacancySearchRequest);

        response.setHeader("Cache-Control", "public, max-age=3600");

        model.addAttribute("jobVacancySearchRequest", jobVacancySearchRequest);
        model.addAttribute("jobVacancies", jobVacancies);


        return "job-vacancy-all";
    }

    @GetMapping("/job-vacancy/{id}")
    public String getJobVacancy(@PathVariable Long id,
                            HttpSession session,
                            Model model) {

        JobVacancyResponse jobVacancy = jobVacancyService.getJobVacancyById(id);
        EmailMessageRequest emailMessageRequest = EmailMessageRequest.builder()
                .emailAddress(jobVacancy.getEmail())
                .subject(userService.getCurrentUser().getEmail() + ": http://localhost:8080/job-vacancy/" + id)
                .build();

        session.setAttribute("emailMessageRequest", emailMessageRequest);

        model.addAttribute("jobVacancy", jobVacancy);
        model.addAttribute("emailMessageRequest", emailMessageRequest);

        return "job-vacancy-detail";
    }
}
