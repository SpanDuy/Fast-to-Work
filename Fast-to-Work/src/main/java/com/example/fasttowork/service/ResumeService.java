package com.example.fasttowork.service;

import com.example.fasttowork.entity.Resume;
import com.example.fasttowork.payload.request.ResumeRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

public interface ResumeService {
    List<Resume> findAllResumes(Long userId);
    Resume findResumeById(Long userId, Long id);
    Resume createResume(ResumeRequest resumeRequest, Long userId);
    void deleteResume(Long userId, Long id);
}
