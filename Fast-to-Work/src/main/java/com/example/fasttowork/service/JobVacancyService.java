package com.example.fasttowork.service;

import com.example.fasttowork.entity.JobVacancy;
import com.example.fasttowork.entity.Resume;
import com.example.fasttowork.payload.request.JobVacancyRequest;
import com.example.fasttowork.payload.request.JobVacancySearchRequest;
import com.example.fasttowork.payload.request.ResumeRequest;

import java.util.List;

public interface JobVacancyService {
    List<JobVacancy> getAllJobVacancy();
    List<JobVacancy> findAllJobVacancy();
    JobVacancy findJobVacancyById(Long id);
    JobVacancy createJobVacancy(JobVacancyRequest jobVacancyRequest, Long userId);
    void editJobVacancy(JobVacancyRequest jobVacancyRequest, Long id);
    void deleteJobVacancy(Long userId, Long id);
    List<JobVacancy> searchJobVacancy(JobVacancySearchRequest jobVacancySearchRequest);
}
