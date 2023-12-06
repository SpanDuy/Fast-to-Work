package com.example.fasttowork.service;

import com.example.fasttowork.entity.Employee;
import com.example.fasttowork.entity.JobVacancy;
import com.example.fasttowork.entity.Resume;
import com.example.fasttowork.payload.request.ResumeRequest;
import com.example.fasttowork.payload.response.ResumeResponse;

import java.util.List;

public interface EmployeeService {
    Employee getCurrentEmployee();
    List<Resume> getCurrentEmployeeResumes();
    ResumeResponse getCurrentEmployeeResumeById(Long id);
    Resume createCurrentEmployeeResume(ResumeRequest resumeRequest);
    void editCurrentEmployeeResume(ResumeRequest resumeRequest, Long id);
    void deleteCurrentEmployeeResume(Long id);
}
