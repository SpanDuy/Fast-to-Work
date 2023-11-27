package com.example.fasttowork.validator;

import com.example.fasttowork.payload.request.JobVacancyRequest;
import com.example.fasttowork.payload.request.ResumeRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ResumeValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return JobVacancyRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ResumeRequest resumeRequest = (ResumeRequest) target;

        if (resumeRequest.getJobType() == "") {
            errors.rejectValue("JobType", "field.required", "Job Type is empty");
        }

        if (resumeRequest.getDescription() == "") {
            errors.rejectValue("Description", "field.required", "Description is empty");
        }
    }
}
