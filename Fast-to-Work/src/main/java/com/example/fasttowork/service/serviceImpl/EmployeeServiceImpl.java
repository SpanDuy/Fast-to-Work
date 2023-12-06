package com.example.fasttowork.service.serviceImpl;

import com.example.fasttowork.entity.Employee;
import com.example.fasttowork.entity.Resume;
import com.example.fasttowork.entity.UserEntity;
import com.example.fasttowork.payload.request.ResumeRequest;
import com.example.fasttowork.payload.response.ResumeResponse;
import com.example.fasttowork.repository.EmployeeRepository;
import com.example.fasttowork.security.SecurityUtil;
import com.example.fasttowork.service.EmployeeService;
import com.example.fasttowork.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.websocket.server.ServerEndpoint;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final SecurityUtil securityUtil;
    private final ResumeService resumeService;

    @Override
    public Employee getCurrentEmployee() {
        String username = securityUtil.getSessionUserEmail();

        return employeeRepository.findByEmail(username);
    }

    @Override
    public List<Resume> getCurrentEmployeeResumes() {
        return resumeService.findAllResumes(getCurrentEmployee());
    }

    @Override
    public ResumeResponse getCurrentEmployeeResumeById(Long id) {
        return resumeService.findResumeById(id, getCurrentEmployee());
    }

    @Override
    public Resume createCurrentEmployeeResume(ResumeRequest resumeRequest) {
        return resumeService.createResume(resumeRequest, getCurrentEmployee());
    }

    @Override
    public void editCurrentEmployeeResume(ResumeRequest resumeRequest, Long id) {
        resumeService.editResume(resumeRequest, id, getCurrentEmployee());
    }

    @Override
    public void deleteCurrentEmployeeResume(Long id) {
        resumeService.deleteResume(id, getCurrentEmployee());
    }
}
