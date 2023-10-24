package com.example.fasttowork.controller;

import com.example.fasttowork.entity.Resume;
import com.example.fasttowork.entity.converter.SkillConverter;
import com.example.fasttowork.payload.request.ResumeRequest;
import com.example.fasttowork.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ResumeController {
    private ResumeService resumeService;

    private SkillConverter skillConverter;

    @Autowired
    public ResumeController(ResumeService resumeService, SkillConverter skillConverter) {
        this.resumeService = resumeService;
        this.skillConverter = skillConverter;
    }

    @GetMapping("/Resume")
    public String getAllResume(Model model) {
        List<Resume> resumes = resumeService.findAllResumes(1L);

        model.addAttribute("resumes", resumes);

        return "resume-list";
    }

    @GetMapping("/Resume/{id}")
    public String getResume(@PathVariable Long id, Model model) {
        Resume resume = resumeService.findResumeById(1L, id);

        model.addAttribute("resume", resume);
        // model.addAttribute("skills", resume.getSkills());

        return "resume-detail";
    }

    @GetMapping("/Resume/new")
    public String createResumeForm(Model model) {
        ResumeRequest resumeRequest = new ResumeRequest(); // Используем ResumeRequest вместо Resume
        model.addAttribute("resume", resumeRequest); // Используем "resume" вместо "resumeRequest"
        return "resume-create";
    }

    @PostMapping("/Resume/new")
    public String createResume(@ModelAttribute("resume") ResumeRequest resumeRequest, BindingResult result, Model model) {
        if(result.hasErrors()) {
            model.addAttribute("resume", resumeRequest);
            return "resume-create";
        }

        resumeService.createResume(resumeRequest, 1L);

        return "redirect:/Resume";
    }

    @GetMapping("/Resume/delete/{id}")
    public String deleteResume(@PathVariable("id") Long id, Model model) {
        resumeService.deleteResume(1L, id);
        return "redirect:/Resume";
    }
}