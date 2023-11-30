package com.example.fasttowork.validator;

import com.example.fasttowork.payload.request.JobVacancyRequest;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class JobVacancyValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return JobVacancyRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        JobVacancyRequest jobVacancyRequest = (JobVacancyRequest) target;

        if (jobVacancyRequest.getJobType() == "") {
            errors.rejectValue("JobType", "field.required", "Job Type is empty");
        }

        if (jobVacancyRequest.getSalary() == null) {
            errors.rejectValue("Salary", "field.required", "Salary is empty");
        }

        if (jobVacancyRequest.getDescription() == "") {
            errors.rejectValue("Description", "field.required", "Description is empty");
        }

        if (jobVacancyRequest.getSkills() == null) {
            errors.rejectValue("Description", "field.required", "Skills is empty");
        }
    }
}
