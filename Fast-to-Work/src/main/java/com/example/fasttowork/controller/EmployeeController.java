package com.example.fasttowork.controller;

import com.example.fasttowork.entity.Employee;
import com.example.fasttowork.entity.JobVacancy;
import com.example.fasttowork.entity.Resume;
import com.example.fasttowork.payload.request.ResumeRequest;
import com.example.fasttowork.payload.response.ResumeResponse;
import com.example.fasttowork.service.EmployeeService;
import com.example.fasttowork.service.ResumeService;
import com.example.fasttowork.validator.ResumeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    private final ResumeValidator resumeValidator;

    @GetMapping("/resume")
    public String getAllResume(Model model) {
        Employee employee = employeeService.getCurrentEmployee();

        model.addAttribute("email", employee.getEmail());
        model.addAttribute("resumes", employee.getResumes());

        return "employee-resume-list";
    }

    @GetMapping("/resume/{id}")
    public String getResume(@PathVariable Long id, Model model) {

        ResumeResponse resume = employeeService.getCurrentEmployeeResumeById(id);

        model.addAttribute("resume", resume);

        return "employee-resume-detail";
    }

    @GetMapping("/resume/create")
    public String createResume(Model model) {
        ResumeRequest resumeRequest = new ResumeRequest();

        model.addAttribute("resume", resumeRequest);

        return "resume-create";
    }

    @PostMapping("/resume/create")
    public String createResume(@ModelAttribute("resume") ResumeRequest resumeRequest,
                               BindingResult result,
                               Model model) {
        resumeValidator.validate(resumeRequest, result);

        if(result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            model.addAttribute("resume", resumeRequest);
            return "resume-create";
        }

        employeeService.createCurrentEmployeeResume(resumeRequest);

        return "redirect:/employee/resume";
    }

    @GetMapping("/resume/edit/{id}")
    public String editResumeForm(@PathVariable Long id,
                                 Model model) {
        ResumeResponse resume = employeeService.getCurrentEmployeeResumeById(id);

        ResumeRequest resumeRequest = ResumeRequest.builder()
                .id(resume.getId())
                .jobType(resume.getJobType())
                .description(resume.getDescription())
                .skills(resume.getSkills())
                .build();

        model.addAttribute("resume", resumeRequest);

        return "resume-edit";
    }

    @PostMapping("/resume/edit/{id}")
    public String editResume(@PathVariable Long id,
                             @ModelAttribute("resume") ResumeRequest resumeRequest,
                             BindingResult result,
                             Model model) {
        resumeValidator.validate(resumeRequest, result);

        if(result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            model.addAttribute("resume", resumeRequest);
            return "resume-edit";
        }

        employeeService.editCurrentEmployeeResume(resumeRequest, id);

        return "redirect:/employee/resume";
    }

    @GetMapping("/resume/delete/{id}")
    public String deleteResume(@PathVariable("id") Long id, Model model) {
        employeeService.deleteCurrentEmployeeResume(id);
        return "redirect:/employee/resume";
    }
}
