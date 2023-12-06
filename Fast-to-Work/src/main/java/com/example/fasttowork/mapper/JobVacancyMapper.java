package com.example.fasttowork.mapper;

import com.example.fasttowork.entity.JobVacancy;
import com.example.fasttowork.payload.request.JobVacancyRequest;
import com.example.fasttowork.payload.response.JobVacancyResponse;

public class JobVacancyMapper {
    public static JobVacancy mapToJobVacancy(JobVacancyRequest jobVacancyRequest) {
        return JobVacancy.builder()
                .id(jobVacancyRequest.getId())
                .jobType(jobVacancyRequest.getJobType())
                .salary(jobVacancyRequest.getSalary())
                .currency(jobVacancyRequest.getCurrency())
                .description(jobVacancyRequest.getDescription())
                .skills(jobVacancyRequest.getNotEmptySkills())
                .build();
    }

    public static JobVacancyResponse mapToJobVacancyResponse(JobVacancy jobVacancy) {
        return JobVacancyResponse.builder()
                .id(jobVacancy.getId())
                .email(jobVacancy.getEmployer().getEmail())
                .company(jobVacancy.getEmployer().getCompany())
                .salary(jobVacancy.getSalary())
                .currency(jobVacancy.getCurrency())
                .jobType(jobVacancy.getJobType())
                .description(jobVacancy.getDescription())
                .skills(jobVacancy.getSkills())
                .build();
    }
}
