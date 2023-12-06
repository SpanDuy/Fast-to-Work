package com.example.fasttowork.mapper;

import com.example.fasttowork.entity.Resume;
import com.example.fasttowork.payload.request.ResumeRequest;
import com.example.fasttowork.payload.response.ResumeResponse;

public class ResumeMapper {
    public static Resume mapToResume(ResumeRequest resumeRequest) {

        return Resume.builder()
                .id(resumeRequest.getId())
                .jobType(resumeRequest.getJobType())
                .description(resumeRequest.getDescription())
                .skills(resumeRequest.getNotEmptySkills())
                .build();
    }

    public static ResumeResponse mapToResumeResponse(Resume resume) {

        return ResumeResponse.builder()
                .id(resume.getId())
                .email(resume.getEmployee().getEmail())
                .name(resume.getEmployee().getName())
                .surname(resume.getEmployee().getSurname())
                .middleName(resume.getEmployee().getMiddleName())
                .birthday(resume.getEmployee().getBirthday())
                .city(resume.getEmployee().getCity())
                .gender(resume.getEmployee().getGender())
                .phoneNumber(resume.getEmployee().getPhoneNumber())
                .jobType(resume.getJobType())
                .description(resume.getDescription())
                .skills(resume.getSkills())
                .build();
    }
}
