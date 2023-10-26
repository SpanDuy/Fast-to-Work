package com.example.fasttowork.service.serviceImpl;

import com.example.fasttowork.entity.*;
import com.example.fasttowork.exception.BadRequestException;
import com.example.fasttowork.payload.request.JobVacancyRequest;
import com.example.fasttowork.repository.EmployeeRepository;
import com.example.fasttowork.repository.EmployerRepository;
import com.example.fasttowork.repository.JobVacancyRepository;
import com.example.fasttowork.repository.ResumeRepository;
import com.example.fasttowork.security.SecurityUtil;
import com.example.fasttowork.service.JobVacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobVacancyServiceImpl implements JobVacancyService {
    @Autowired
    private JobVacancyRepository jobVacancyRepository;

    @Autowired
    private EmployerRepository employerRepository;

    @Override
    public List<JobVacancy> findAllJobVacancy() {
        String username = SecurityUtil.getSessionUser();
        UserEntity user = employerRepository.findByEmail(username);

        List<JobVacancy> jobVacancies = jobVacancyRepository.findByEmployerId(user.getId());

        return jobVacancies;
    }


    @Override
    public JobVacancy findJobVacancyById(Long id) {
        JobVacancy jobVacancy = jobVacancyRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("RESUME_DOES_NOT_BELONG_TO_USER"));

        return jobVacancy;
    }

    @Override
    public JobVacancy createJobVacancy(JobVacancyRequest jobVacancyRequest, Long userId) {
        String username = SecurityUtil.getSessionUser();
        Employer employer = employerRepository.findByEmail(username);

        JobVacancy jobVacancy = new JobVacancy();

        jobVacancy.setEmployer(employer);
        jobVacancy.setCompany(employer.getCompany());

        jobVacancy.setJobType(jobVacancyRequest.getJobType());
        jobVacancy.setSalary(jobVacancyRequest.getSalary());
        jobVacancy.setDescription(jobVacancyRequest.getDescription());
        jobVacancy.setSkills(jobVacancyRequest.getSkills());

        jobVacancyRepository.save(jobVacancy);

        employer.getJobVacancies().add(jobVacancy);
        employerRepository.save(employer);

        return jobVacancy;
    }

    @Override
    public void editJobVacancy(JobVacancyRequest jobVacancyRequest, Long id) {
        JobVacancy jobVacancy = jobVacancyRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("RESUME_DOES_NOT_BELONG_TO_USER"));

        jobVacancy.setJobType(jobVacancyRequest.getJobType());
        jobVacancy.setSalary(jobVacancyRequest.getSalary());
        jobVacancy.setDescription(jobVacancyRequest.getDescription());
        jobVacancy.setSkills(jobVacancyRequest.getSkills());

        jobVacancyRepository.save(jobVacancy);
    }


    @Override
    public void deleteJobVacancy(Long userId, Long id) {
        jobVacancyRepository.deleteById(id);
    }

}
