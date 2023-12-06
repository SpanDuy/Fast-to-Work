package com.example.fasttowork.service;

import com.example.fasttowork.entity.Employer;
import com.example.fasttowork.entity.JobVacancy;
import com.example.fasttowork.entity.Resume;
import com.example.fasttowork.payload.request.JobVacancyRequest;
import com.example.fasttowork.payload.request.JobVacancySearchRequest;
import com.example.fasttowork.payload.request.ResumeRequest;
import com.example.fasttowork.payload.response.JobVacancyResponse;

import java.util.List;

public interface JobVacancyService {
    List<JobVacancy> getAllJobVacancy();
    List<JobVacancy> findAllJobVacancy(Employer employer);
    JobVacancyResponse findJobVacancyById(Long id, Employer employer);
    JobVacancyResponse getJobVacancyById(Long id);
    JobVacancy createJobVacancy(JobVacancyRequest jobVacancyRequest, Employer employer);
    void editJobVacancy(JobVacancyRequest jobVacancyRequest, Long id, Employer employer);
    void deleteJobVacancy(Long id, Employer employer);
    List<JobVacancy> searchJobVacancy(JobVacancySearchRequest jobVacancySearchRequest);
}
