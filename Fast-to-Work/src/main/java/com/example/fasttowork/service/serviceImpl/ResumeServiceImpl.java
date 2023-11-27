package com.example.fasttowork.service.serviceImpl;

import com.example.fasttowork.entity.Employee;
import com.example.fasttowork.entity.Resume;
import com.example.fasttowork.entity.UserEntity;
import com.example.fasttowork.exception.BadRequestException;
import com.example.fasttowork.payload.request.ResumeRequest;
import com.example.fasttowork.repository.EmployeeRepository;
import com.example.fasttowork.repository.ResumeRepository;
import com.example.fasttowork.security.SecurityUtil;
import com.example.fasttowork.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<Resume> getAllResumes() {
        List<Resume> resumes = resumeRepository.findAll();

        return resumes;
    }

    @Override
    public List<Resume> findAllResumes() {
        String username = SecurityUtil.getSessionUserEmail();
        UserEntity user = employeeRepository.findByEmail(username);

        List<Resume> resumes = resumeRepository.findByEmployeeId(user.getId());

        return resumes;
    }


    @Override
    public Resume findResumeById(Long id) {
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("RESUME_DOES_NOT_BELONG_TO_USER"));

        return resume;
    }

    @Override
    public Resume createResume(ResumeRequest resumeRequest, Long userId) {
        String username = SecurityUtil.getSessionUserEmail();
        Employee employee = employeeRepository.findByEmail(username);

        Resume resume = new Resume();

        resume.setEmployee(employee);
        resume.setJobType(resumeRequest.getJobType());
        resume.setName(employee.getName());
        resume.setSurname(employee.getSurname());
        resume.setMiddleName(employee.getMiddleName());
        resume.setBirthday(employee.getBirthday());
        resume.setCity(employee.getCity());
        resume.setGender(employee.getGender());
        resume.setPhoneNumber(employee.getPhoneNumber());
        resume.setSkills(resumeRequest.getSkills());
        resume.setDescription(resumeRequest.getDescription());

        resumeRepository.save(resume);

        employee.getResumes().add(resume);
        employeeRepository.save(employee);

        return resume;
    }

    @Override
    public void editResume(ResumeRequest resumeRequest, Long id) {
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("RESUME_DOES_NOT_BELONG_TO_USER"));

        resume.setJobType(resumeRequest.getJobType());
        resume.setDescription(resumeRequest.getDescription());
        resume.setSkills(resumeRequest.getSkills());

        resumeRepository.save(resume);
    }

    @Override
    public void deleteResume(Long userId, Long id) {
        resumeRepository.deleteById(id);
    }
}
