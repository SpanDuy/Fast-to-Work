package com.example.fasttowork.controller;

import com.example.fasttowork.entity.Employer;
import com.example.fasttowork.entity.JobVacancy;
import com.example.fasttowork.payload.request.JobVacancyRequest;
import com.example.fasttowork.payload.request.JobVacancySearchRequest;
import com.example.fasttowork.payload.response.JobVacancyResponse;
import com.example.fasttowork.service.EmployeeService;
import com.example.fasttowork.service.EmployerService;
import com.example.fasttowork.validator.JobVacancyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/employer")
@RequiredArgsConstructor
public class EmployerController {
    private final EmployerService employerService;
    private final JobVacancyValidator jobVacancyValidator;

    @GetMapping("/job-vacancy")
    public String findAllJobVacancy(Model model) {
        Employer employer = employerService.getCurrentEmployer();

        model.addAttribute("email", employer.getEmail());
        model.addAttribute("jobVacancies", employer.getJobVacancies());

        return "employer-job-vacancy-list";
    }

    @GetMapping("/job-vacancy/{id}")
    public String getJobVacancy(@PathVariable Long id, Model model) {

        JobVacancyResponse jobVacancy = employerService.getCurrentEmployerJobVacancyById(id);

        model.addAttribute("jobVacancy", jobVacancy);

        return "employer-job-vacancy-detail";
    }

    @GetMapping("/job-vacancy/create")
    public String createJobVacancyForm(Model model) {
        JobVacancyRequest jobVacancyRequest = new JobVacancyRequest();

        model.addAttribute("jobVacancyRequest", jobVacancyRequest);

        return "job-vacancy-create";
    }

    @PostMapping("/job-vacancy/create")
    public String createJobVacancyRequest(@ModelAttribute("jobVacancy") JobVacancyRequest jobVacancyRequest,
                                          BindingResult result,
                                          Model model) {
        jobVacancyValidator.validate(jobVacancyRequest, result);

        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            model.addAttribute("jobVacancyRequest", jobVacancyRequest);
            return "job-vacancy-create";
        }

        employerService.createCurrentEmployerJobVacancy(jobVacancyRequest);

        return "redirect:/employer/job-vacancy";
    }

    @GetMapping("/job-vacancy/edit/{id}")
    public String editJobVacancyForm(@PathVariable Long id,
                                     Model model) {
        JobVacancyResponse jobVacancy = employerService.getCurrentEmployerJobVacancyById(id);

        JobVacancyRequest jobVacancyRequest = JobVacancyRequest.builder()
                .id(jobVacancy.getId())
                .jobType(jobVacancy.getJobType())
                .skills(jobVacancy.getSkills())
                .description(jobVacancy.getDescription())
                .salary(jobVacancy.getSalary())
                .currency(jobVacancy.getCurrency())
                .build();

        model.addAttribute("jobVacancyRequest", jobVacancyRequest);

        return "job-vacancy-edit";
    }

    @PostMapping("/job-vacancy/edit/{id}")
    public String editJobVacancy(@PathVariable Long id,
                                 @ModelAttribute("jobVacancyRequest") JobVacancyRequest jobVacancyRequest,
                                 BindingResult result,
                                 Model model) {
        jobVacancyValidator.validate(jobVacancyRequest, result);

        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            model.addAttribute("jobVacancyRequest", jobVacancyRequest);
            return "job-vacancy-edit";
        }

        employerService.editCurrentEmployerJobVacancy(jobVacancyRequest, id);

        return "redirect:/employer/job-vacancy";
    }

    @GetMapping("/job-vacancy/delete/{id}")
    public String deleteJobVacancy(@PathVariable("id") Long id, Model model) {
        employerService.deleteCurrentEmployerJobVacancy(id);
        return "redirect:/employer/job-vacancy";
    }
}
