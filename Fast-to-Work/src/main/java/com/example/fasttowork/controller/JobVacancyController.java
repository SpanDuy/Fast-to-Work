package com.example.fasttowork.controller;

import com.example.fasttowork.entity.JobVacancy;
import com.example.fasttowork.entity.Skill;
import com.example.fasttowork.entity.converter.SkillConverter;
import com.example.fasttowork.payload.request.JobVacancyRequest;
import com.example.fasttowork.payload.request.JobVacancySearchRequest;
import com.example.fasttowork.security.SecurityUtil;
import com.example.fasttowork.service.JobVacancyService;
import com.example.fasttowork.validator.JobVacancyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class JobVacancyController {
    private JobVacancyService jobVacancyService;

    private SkillConverter skillConverter;

    private JobVacancyValidator jobVacancyValidator;

    private SecurityUtil securityUtil =  new SecurityUtil();

    @Autowired
    public JobVacancyController(JobVacancyService jobVacancyService,
                                SkillConverter skillConverter,
                                JobVacancyValidator jobVacancyValidator) {
        this.jobVacancyService = jobVacancyService;
        this.skillConverter = skillConverter;
        this.jobVacancyValidator = jobVacancyValidator;
    }

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
        if(result.hasErrors()) {
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

    @GetMapping("/job-vacancy")
    public String findAllJobVacancy(Model model) {
        List<JobVacancy> jobVacancies = jobVacancyService.findAllJobVacancy();
        String email = securityUtil.getSessionUserEmail();

        model.addAttribute("email", email);
        model.addAttribute("jobVacancies", jobVacancies);

        return "job-vacancy-list";
    }

    @GetMapping("/job-vacancy/{id}")
    public String getJobVacancy(@PathVariable Long id, Model model, HttpServletRequest request) {
        JobVacancy jobVacancy = jobVacancyService.findJobVacancyById(id);
        String referrer = request.getHeader("Referer");

        model.addAttribute("referrer", referrer);
        model.addAttribute("jobVacancy", jobVacancy);

        return "job-vacancy-detail";
    }

    @GetMapping("/job-vacancy/new")
    public String createJobVacancyForm(Model model) {
        JobVacancyRequest jobVacancyRequest = new JobVacancyRequest(); // Используем ResumeRequest вместо Resume
        model.addAttribute("jobVacancyRequest", jobVacancyRequest); // Используем "resume" вместо "resumeRequest"
        return "job-vacancy-create";
    }

    @PostMapping("/job-vacancy/new")
    public String createJobVacancyRequest(@ModelAttribute("resume") JobVacancyRequest jobVacancyRequest,
                                          BindingResult result,
                                          Model model) {
        jobVacancyValidator.validate(jobVacancyRequest, result);

        if(result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            model.addAttribute("jobVacancyRequest", jobVacancyRequest);
            return "job-vacancy-create";
        }

        jobVacancyService.createJobVacancy(jobVacancyRequest, 1L);

        return "redirect:/job-vacancy";
    }

    @GetMapping("/job-vacancy/edit/{id}")
    public String editJobVacancyForm(@PathVariable Long id,
                                 Model model) {
        JobVacancy jobVacancy = jobVacancyService.findJobVacancyById(id);

        JobVacancyRequest jobVacancyRequest = JobVacancyRequest.builder()
                .id(jobVacancy.getId())
                .jobType(jobVacancy.getJobType())
                .skills(jobVacancy.getSkills())
                .description(jobVacancy.getDescription())
                .salary(jobVacancy.getSalary())
                .currency(jobVacancy.getCurrency())
                .build();

        model.addAttribute("size", jobVacancyRequest.getSkills().size());
        model.addAttribute("jobVacancyRequest", jobVacancyRequest); // Используем "resume" вместо "resumeRequest"
        return "job-vacancy-edit";
    }

    @PostMapping("/job-vacancy/edit/{id}")
    public String editJobVacancy(@PathVariable Long id,
                             @ModelAttribute("jobVacancyRequest") JobVacancyRequest jobVacancyRequest,
                             BindingResult result,
                             Model model) {
        jobVacancyValidator.validate(jobVacancyRequest, result);

        if(result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            model.addAttribute("skillList", jobVacancyRequest.getSkills());
            model.addAttribute("jobVacancyRequest", jobVacancyRequest);

//            for (Skill skill : jobVacancyRequest.getSkills()) {
//
//            }

            return "job-vacancy-edit";
        }

        jobVacancyService.editJobVacancy(jobVacancyRequest, id);

        return "redirect:/job-vacancy";
    }

    @GetMapping("/job-vacancy/delete/{id}")
    public String deleteJobVacancy(@PathVariable("id") Long id, Model model) {
        jobVacancyService.deleteJobVacancy(1L, id);
        return "redirect:/job-vacancy";
    }
}
