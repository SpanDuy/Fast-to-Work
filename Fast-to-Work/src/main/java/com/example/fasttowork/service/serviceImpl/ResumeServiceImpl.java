package com.example.fasttowork.service.serviceImpl;

import com.example.fasttowork.entity.*;
import com.example.fasttowork.exception.BadRequestException;
import com.example.fasttowork.mapper.ResumeMapper;
import com.example.fasttowork.payload.request.JobVacancySearchRequest;
import com.example.fasttowork.payload.request.ResumeRequest;
import com.example.fasttowork.payload.request.ResumeSearchRequest;
import com.example.fasttowork.payload.response.ResumeResponse;
import com.example.fasttowork.repository.EmployeeRepository;
import com.example.fasttowork.repository.ResumeRepository;
import com.example.fasttowork.security.SecurityUtil;
import com.example.fasttowork.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {
    private final EntityManager entityManager;
    private final ResumeRepository resumeRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public List<Resume> getAllResumes() {
        return resumeRepository.findAll();
    }

    @Override
    public List<Resume> findAllResumes(Employee employee) {
        return resumeRepository.findByEmployeeId(employee.getId());
    }

    @Override
    public ResumeResponse findResumeById(Long id, Employee employee) {
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("RESUME_DOES_NOT_BELONG_TO_USER"));

        if (!resume.getEmployee().equals(employee)) {
            throw new BadRequestException("RESUME_DOES_NOT_BELONG_TO_USER");
        }

        return ResumeMapper.mapToResumeResponse(resume);
    }

    @Override
    public ResumeResponse getResumeById(Long id) {
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("RESUME_DOES_NOT_BELONG_TO_USER"));

        return ResumeMapper.mapToResumeResponse(resume);
    }

    @Override
    public Resume createResume(ResumeRequest resumeRequest, Employee employee) {
        Resume resume = ResumeMapper.mapToResume(resumeRequest);
        resume.setEmployee(employee);

        resumeRepository.save(resume);

        employee.getResumes().add(resume);
        employeeRepository.save(employee);

        return resume;
    }

    @Override
    public void editResume(ResumeRequest resumeRequest, Long id, Employee employee) {
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("RESUME_DOES_NOT_BELONG_TO_USER"));

        if (!resume.getEmployee().equals(employee)) {
            throw new BadRequestException("RESUME_DOES_NOT_BELONG_TO_USER");
        }

        resume = ResumeMapper.mapToResume(resumeRequest);
        resume.setEmployee(employee);

        resumeRepository.save(resume);
    }

    @Override
    public void deleteResume(Long id, Employee employee) {
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("RESUME_DOES_NOT_BELONG_TO_USER"));

        if (!resume.getEmployee().equals(employee)) {
            throw new BadRequestException("RESUME_DOES_NOT_BELONG_TO_USER");
        }

        resumeRepository.deleteById(id);
    }

    @Override
    public List<Resume> searchResume(ResumeSearchRequest resumeSearchRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Resume> criteriaQuery = criteriaBuilder.createQuery(Resume.class);
        Root<Resume> root = criteriaQuery.from(Resume.class);
        LocalDateTime ageMin = LocalDateTime.now();
        LocalDateTime ageMax = LocalDateTime.now();

        List<Predicate> predicates = new ArrayList<>();

        if (resumeSearchRequest.getAgeMin() != null) {
            ageMin = ageMin.minusYears(resumeSearchRequest.getAgeMin());
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("employee").get("birthday"), ageMax));
        }

        if (resumeSearchRequest.getAgeMax() != null) {
            ageMax = ageMax.minusYears(resumeSearchRequest.getAgeMax());
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("employee").get("birthday"), ageMin));
        }

        if (StringUtils.isNotBlank(resumeSearchRequest.getJobType())) {
            predicates.add(criteriaBuilder.equal(root.get("jobType"), resumeSearchRequest.getJobType()));
        }

        if (StringUtils.isNotBlank(resumeSearchRequest.getCity())) {
            predicates.add(criteriaBuilder.equal(root.get("city"), resumeSearchRequest.getCity()));
        }

        if (resumeSearchRequest.getSkills() != null) {
            resumeSearchRequest.setSkills(resumeSearchRequest.getSkills().stream()
                    .filter(item -> item != null && item.getSkill() != "")
                    .collect(Collectors.toList()));
        }

        if (resumeSearchRequest.getSkills() != null && resumeSearchRequest.getSkills().size() > 0) {
            Join<JobVacancy, Skill> skillJoin = root.join("skills");
            List<String> skillNames = resumeSearchRequest.getSkills().stream()
                    .map(Skill::getSkill)
                    .collect(Collectors.toList());

            predicates.add(skillJoin.get("skill").in(skillNames));

            if (resumeSearchRequest.getSkills().size() > 1) {
                criteriaQuery.groupBy(root.get("id"));

                criteriaQuery.having(
                        criteriaBuilder.equal(
                                criteriaBuilder.countDistinct(skillJoin.get("skill")),
                                resumeSearchRequest.getSkills().size()
                        )
                );
            }
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Resume> typedQuery = entityManager.createQuery(criteriaQuery);

        return typedQuery.getResultList();
    }
}
