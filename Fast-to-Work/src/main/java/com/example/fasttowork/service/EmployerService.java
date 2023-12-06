package com.example.fasttowork.service;

import com.example.fasttowork.entity.Employee;
import com.example.fasttowork.entity.Employer;
import com.example.fasttowork.entity.JobVacancy;
import com.example.fasttowork.entity.Resume;
import com.example.fasttowork.payload.request.JobVacancyRequest;
import com.example.fasttowork.payload.request.ResumeRequest;
import com.example.fasttowork.payload.response.JobVacancyResponse;
import com.example.fasttowork.payload.response.ResumeResponse;

import java.util.List;

public interface EmployerService {
    Employer getCurrentEmployer();
    List<JobVacancy> getCurrentEmployerJobVacancy();
    JobVacancyResponse getCurrentEmployerJobVacancyById(Long id);
    JobVacancy createCurrentEmployerJobVacancy(JobVacancyRequest jobVacancyRequest);
    void editCurrentEmployerJobVacancy(JobVacancyRequest jobVacancyRequest, Long id);
    void deleteCurrentEmployerJobVacancy(Long id);
}
