package com.example.fasttowork.service.serviceImpl;

import com.example.fasttowork.entity.Resume;
import com.example.fasttowork.entity.UserEntity;
import com.example.fasttowork.exception.BadRequestException;
import com.example.fasttowork.payload.request.ResumeRequest;
import com.example.fasttowork.repository.ResumeRepository;
import com.example.fasttowork.repository.UserRepository;
import com.example.fasttowork.security.SecurityUtil;
import com.example.fasttowork.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Resume> findAllResumes(Long userId) {
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userRepository.findByEmail(username);

        List<Resume> resumes = resumeRepository.findByUserId(user.getId());

        return resumes;
    }


    @Override
    public Resume findResumeById(Long userId, Long id) {
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("RESUME_DOES_NOT_BELONG_TO_USER"));

        return resume;
    }

    @Override
    public Resume createResume(ResumeRequest resumeRequest, Long userId) {
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userRepository.findByEmail(username);

        Resume resume = new Resume();

        resume.setUser(user);
        resume.setJobType(resumeRequest.getJobType());
        resume.setName(user.getName());
        resume.setSurname(user.getSurname());
        resume.setMiddleName(user.getMiddleName());
        resume.setBirthday(user.getBirthday());
        resume.setCity(user.getCity());
        resume.setGender(user.getGender());
        resume.setPhoneNumber(user.getPhoneNumber());
        resume.setSkills(resumeRequest.getSkills());
        resume.setGitLink(resumeRequest.getGitLink());

        resumeRepository.save(resume);

        return resume;
    }

    @Override
    public void deleteResume(Long userId, Long id) {
        resumeRepository.deleteById(id);
    }
}
