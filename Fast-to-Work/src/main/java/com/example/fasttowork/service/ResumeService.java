package com.example.fasttowork.service;

import com.example.fasttowork.entity.JobVacancy;
import com.example.fasttowork.entity.Resume;
import com.example.fasttowork.payload.request.JobVacancySearchRequest;
import com.example.fasttowork.payload.request.ResumeRequest;
import com.example.fasttowork.payload.request.ResumeSearchRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

public interface ResumeService {
    List<Resume> getAllResumes();
    List<Resume> findAllResumes();
    Resume findResumeById(Long id);
    Resume createResume(ResumeRequest resumeRequest, Long userId);
    void editResume(ResumeRequest resumeRequest, Long id);
    void deleteResume(Long userId, Long id);
    List<Resume> searchResume(ResumeSearchRequest ResumeSearchRequest);
}
