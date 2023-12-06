package com.example.fasttowork.service.serviceImpl;

import com.example.fasttowork.entity.Employee;
import com.example.fasttowork.entity.Employer;
import com.example.fasttowork.entity.JobVacancy;
import com.example.fasttowork.payload.request.JobVacancyRequest;
import com.example.fasttowork.payload.response.JobVacancyResponse;
import com.example.fasttowork.repository.EmployerRepository;
import com.example.fasttowork.security.SecurityUtil;
import com.example.fasttowork.service.EmployerService;
import com.example.fasttowork.service.JobVacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployerServiceImpl implements EmployerService {

    private final EmployerRepository employerRepository;
    private final SecurityUtil securityUtil;
    private final JobVacancyService jobVacancyService;

    @Override
    public Employer getCurrentEmployer() {
        String username = securityUtil.getSessionUserEmail();

        return employerRepository.findByEmail(username);
    }

    @Override
    public List<JobVacancy> getCurrentEmployerJobVacancy() {
        return jobVacancyService.findAllJobVacancy(getCurrentEmployer());
    }

    @Override
    public JobVacancyResponse getCurrentEmployerJobVacancyById(Long id) {
        return jobVacancyService.findJobVacancyById(id, getCurrentEmployer());
    }

    @Override
    public JobVacancy createCurrentEmployerJobVacancy(JobVacancyRequest jobVacancyRequest) {
        return jobVacancyService.createJobVacancy(jobVacancyRequest, getCurrentEmployer());
    }

    @Override
    public void editCurrentEmployerJobVacancy(JobVacancyRequest jobVacancyRequest, Long id) {
        jobVacancyService.editJobVacancy(jobVacancyRequest, id, getCurrentEmployer());
    }

    @Override
    public void deleteCurrentEmployerJobVacancy(Long id) {
        jobVacancyService.deleteJobVacancy(id, getCurrentEmployer());
    }
}
