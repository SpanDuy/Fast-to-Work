package com.example.fasttowork.service;

import com.example.fasttowork.entity.Employee;
import com.example.fasttowork.entity.JobVacancy;
import com.example.fasttowork.entity.Resume;
import com.example.fasttowork.payload.request.JobVacancySearchRequest;
import com.example.fasttowork.payload.request.ResumeRequest;
import com.example.fasttowork.payload.request.ResumeSearchRequest;
import com.example.fasttowork.payload.response.ResumeResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

public interface ResumeService {
    List<Resume> getAllResumes();
    List<Resume> findAllResumes(Employee employee);
    ResumeResponse findResumeById(Long id, Employee employee);
    ResumeResponse getResumeById(Long id);
    Resume createResume(ResumeRequest resumeRequest, Employee employee);
    void editResume(ResumeRequest resumeRequest, Long id, Employee employee);
    void deleteResume(Long id, Employee employee);
    List<Resume> searchResume(ResumeSearchRequest ResumeSearchRequest);
}
