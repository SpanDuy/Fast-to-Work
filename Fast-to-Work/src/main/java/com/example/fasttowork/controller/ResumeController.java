package com.example.fasttowork.controller;

import com.example.fasttowork.entity.JobVacancy;
import com.example.fasttowork.entity.Resume;
import com.example.fasttowork.entity.converter.SkillConverter;
import com.example.fasttowork.payload.request.EmailMessageRequest;
import com.example.fasttowork.payload.request.JobVacancySearchRequest;
import com.example.fasttowork.payload.request.ResumeRequest;
import com.example.fasttowork.payload.request.ResumeSearchRequest;
import com.example.fasttowork.payload.response.JobVacancyResponse;
import com.example.fasttowork.payload.response.ResumeResponse;
import com.example.fasttowork.security.SecurityUtil;
import com.example.fasttowork.service.ResumeService;
import com.example.fasttowork.service.UserService;
import com.example.fasttowork.validator.ResumeValidator;
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
public class ResumeController {
    private final ResumeService resumeService;
    private final UserService userService;

    @GetMapping("/resume/all")
    public String getAllResume(Model model,
                               HttpServletResponse response) {
        ResumeSearchRequest resumeSearchRequest = new ResumeSearchRequest();
        List<Resume> resumes = resumeService.getAllResumes();

        response.setHeader("Cache-Control", "public, max-age=3600");

        model.addAttribute("resumeSearchRequest", resumeSearchRequest);
        model.addAttribute("resumes", resumes);
        return "resume-all";
    }

    @PostMapping("/resume/all")
    public String getAllResume(@ModelAttribute("resume") ResumeSearchRequest resumeSearchRequest,
                                   BindingResult result,
                                   Model model,
                                   HttpServletResponse response) {
        if(result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            model.addAttribute("resumeSearchRequest", resumeSearchRequest);
            return "resume-all";
        }

        List<Resume> resumes = resumeService.searchResume(resumeSearchRequest);

        response.setHeader("Cache-Control", "public, max-age=3600");

        model.addAttribute("resumeSearchRequest", resumeSearchRequest);
        model.addAttribute("resumes", resumes);

        return "resume-all";
    }

    @GetMapping("/resume/{id}")
    public String getResume(@PathVariable Long id,
                                HttpSession session,
                                Model model) {

        ResumeResponse resume = resumeService.getResumeById(id);
        EmailMessageRequest emailMessageRequest = EmailMessageRequest.builder()
                .emailAddress(resume.getEmail())
                .subject(userService.getCurrentUser().getEmail() + ": http://localhost:8080/resume/" + id)
                .build();

        session.setAttribute("emailMessageRequest", emailMessageRequest);

        model.addAttribute("resume", resume);
        model.addAttribute("emailMessageRequest", emailMessageRequest);

        return "resume-detail";
    }
}
