package com.example.fasttowork.service.serviceImpl;

import com.example.fasttowork.entity.*;
import com.example.fasttowork.exception.BadRequestException;
import com.example.fasttowork.payload.request.JobVacancyRequest;
import com.example.fasttowork.payload.request.JobVacancySearchRequest;
import com.example.fasttowork.repository.EmployerRepository;
import com.example.fasttowork.repository.JobVacancyRepository;
import com.example.fasttowork.security.SecurityUtil;
import com.example.fasttowork.service.CurrencyService;
import com.example.fasttowork.service.JobVacancyService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobVacancyServiceImpl implements JobVacancyService {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JobVacancyRepository jobVacancyRepository;

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private CurrencyService currencyService;

    @Override
    public List<JobVacancy> getAllJobVacancy() {
        List<JobVacancy> jobVacancies = jobVacancyRepository.findAll();

        return jobVacancies;
    }

    @Override
    public List<JobVacancy> findAllJobVacancy() {
        String username = SecurityUtil.getSessionUserEmail();
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
        String username = SecurityUtil.getSessionUserEmail();
        Employer employer = employerRepository.findByEmail(username);

        JobVacancy jobVacancy = new JobVacancy();

        jobVacancy.setEmployer(employer);
        jobVacancy.setCompany(employer.getCompany());

        jobVacancy.setJobType(jobVacancyRequest.getJobType());
        jobVacancy.setSalary(jobVacancyRequest.getSalary());
        jobVacancy.setCurrency(jobVacancyRequest.getCurrency());
        jobVacancy.setDescription(jobVacancyRequest.getDescription());
        if (jobVacancyRequest.getSkills() != null) {
            jobVacancyRequest.setSkills(jobVacancyRequest.getSkills().stream()
                    .filter(item -> item != null && item.getSkill() != "")
                    .collect(Collectors.toList()));
        }
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
        jobVacancy.setCurrency(jobVacancy.getCurrency());
        jobVacancy.setDescription(jobVacancyRequest.getDescription());

        jobVacancyRequest.setSkills(jobVacancyRequest.getSkills().stream()
                .filter(item -> item != null && item.getSkill() != "")
                .collect(Collectors.toList()));
        jobVacancy.setSkills(jobVacancyRequest.getSkills());

        jobVacancyRepository.save(jobVacancy);
    }

    @Override
    public void deleteJobVacancy(Long userId, Long id) {
        jobVacancyRepository.deleteById(id);
    }

    @Override
    public List<JobVacancy> searchJobVacancy(JobVacancySearchRequest jobVacancySearchRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<JobVacancy> criteriaQuery = criteriaBuilder.createQuery(JobVacancy.class);
        Root<JobVacancy> root = criteriaQuery.from(JobVacancy.class);
        Double currentOfficialRate = currencyService.getCurrentOfficialRate();
        Expression<Double> adjustedSalaryExpression = (Expression<Double>) criteriaBuilder.selectCase()
                .when(criteriaBuilder.equal(root.get("currency"), "BYN"),
                        criteriaBuilder.quot(root.get("salary"), currentOfficialRate))
                .otherwise(root.get("salary"))
                .as(Double.class)
                .alias("adjustedSalary");

        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.isNotBlank(jobVacancySearchRequest.getJobType())) {
            predicates.add(criteriaBuilder.equal(root.get("jobType"), jobVacancySearchRequest.getJobType()));
        }

        if (jobVacancySearchRequest.getSalaryMin() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(adjustedSalaryExpression,
                    jobVacancySearchRequest.getCurrencyMin().equals("BYN") ?
                            jobVacancySearchRequest.getSalaryMin().doubleValue() / currentOfficialRate :
                            jobVacancySearchRequest.getSalaryMin().doubleValue()));
        }

        if (jobVacancySearchRequest.getSalaryMax() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(adjustedSalaryExpression,
                    jobVacancySearchRequest.getCurrencyMax().equals("BYN") ?
                            jobVacancySearchRequest.getSalaryMax().doubleValue() / currentOfficialRate :
                            jobVacancySearchRequest.getSalaryMax().doubleValue()));
        }

        if (jobVacancySearchRequest.getSkills() != null) {
            jobVacancySearchRequest.setSkills(jobVacancySearchRequest.getSkills().stream()
                    .filter(item -> item != null && !item.getSkill().isEmpty())
                    .collect(Collectors.toList()));
        }

        if (jobVacancySearchRequest.getSkills() != null && !jobVacancySearchRequest.getSkills().isEmpty()) {
            Join<JobVacancy, Skill> skillJoin = root.join("skills");
            List<String> skillNames = jobVacancySearchRequest.getSkills().stream()
                    .map(Skill::getSkill)
                    .collect(Collectors.toList());

            predicates.add(skillJoin.get("skill").in(skillNames));

            if (jobVacancySearchRequest.getSkills().size() > 1) {
                criteriaQuery.groupBy(root.get("id"));

                criteriaQuery.having(
                        criteriaBuilder.equal(
                                criteriaBuilder.countDistinct(skillJoin.get("skill")),
                                jobVacancySearchRequest.getSkills().size()
                        )
                );
            }
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        TypedQuery<JobVacancy> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }
}
