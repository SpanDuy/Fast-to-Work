package com.example.fasttowork.service.serviceImpl;

import com.example.fasttowork.entity.*;
import com.example.fasttowork.exception.BadRequestException;
import com.example.fasttowork.mapper.JobVacancyMapper;
import com.example.fasttowork.payload.request.JobVacancyRequest;
import com.example.fasttowork.payload.request.JobVacancySearchRequest;
import com.example.fasttowork.payload.response.JobVacancyResponse;
import com.example.fasttowork.repository.EmployerRepository;
import com.example.fasttowork.repository.JobVacancyRepository;
import com.example.fasttowork.security.SecurityUtil;
import com.example.fasttowork.service.CurrencyService;
import com.example.fasttowork.service.JobVacancyService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class JobVacancyServiceImpl implements JobVacancyService {

    private final EntityManager entityManager;
    private final JobVacancyRepository jobVacancyRepository;
    private final EmployerRepository employerRepository;
    private final CurrencyService currencyService;

    @Override
    public List<JobVacancy> getAllJobVacancy() {
        return jobVacancyRepository.findAll();
    }

    @Override
    public List<JobVacancy> findAllJobVacancy(Employer employer) {
        return jobVacancyRepository.findByEmployerId(employer.getId());
    }


    @Override
    public JobVacancyResponse findJobVacancyById(Long id, Employer employer) {
        JobVacancy jobVacancy = jobVacancyRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("RESUME_DOES_NOT_BELONG_TO_USER"));

        if (!jobVacancy.getEmployer().equals(employer)) {
            throw new BadRequestException("RESUME_DOES_NOT_BELONG_TO_USER");
        }

        return JobVacancyMapper.mapToJobVacancyResponse(jobVacancy);
    }

    @Override
    public JobVacancyResponse getJobVacancyById(Long id) {
        JobVacancy jobVacancy = jobVacancyRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("RESUME_DOES_NOT_BELONG_TO_USER"));

        return JobVacancyMapper.mapToJobVacancyResponse(jobVacancy);
    }

    @Override
    public JobVacancy createJobVacancy(JobVacancyRequest jobVacancyRequest, Employer employer) {
        JobVacancy jobVacancy = JobVacancyMapper.mapToJobVacancy(jobVacancyRequest);
        jobVacancy.setEmployer(employer);

        jobVacancyRepository.save(jobVacancy);

        employer.getJobVacancies().add(jobVacancy);
        employerRepository.save(employer);

        return jobVacancy;
    }

    @Override
    public void editJobVacancy(JobVacancyRequest jobVacancyRequest, Long id, Employer employer) {
        JobVacancy jobVacancy = jobVacancyRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("RESUME_DOES_NOT_BELONG_TO_USER"));

        if (!jobVacancy.getEmployer().equals(employer)) {
            throw new BadRequestException("RESUME_DOES_NOT_BELONG_TO_USER");
        }

        jobVacancyRequest.setId(jobVacancy.getId());
        jobVacancy = JobVacancyMapper.mapToJobVacancy(jobVacancyRequest);
        jobVacancy.setEmployer(employer);

        jobVacancyRepository.save(jobVacancy);
    }

    @Override
    public void deleteJobVacancy(Long id, Employer employer) {
        JobVacancy jobVacancy = jobVacancyRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("RESUME_DOES_NOT_BELONG_TO_USER"));

        if (!jobVacancy.getEmployer().equals(employer)) {
            throw new BadRequestException("RESUME_DOES_NOT_BELONG_TO_USER");
        }

        jobVacancyRepository.deleteById(id);
    }

    public Expression<Double> addAdjustedSalary(CriteriaBuilder criteriaBuilder, Root<JobVacancy> root) {
        return (Expression<Double>) criteriaBuilder.selectCase()
                .when(criteriaBuilder.equal(root.get("currency"), "BYN"),
                        criteriaBuilder.quot(root.get("salary"), currencyService.getCurrencyFromNBRB().getCurOfficialRate()))
                .otherwise(root.get("salary"))
                .as(Double.class)
                .alias("adjustedSalary");
    }

    @Override
    public List<JobVacancy> searchJobVacancy(JobVacancySearchRequest jobVacancySearchRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<JobVacancy> criteriaQuery = criteriaBuilder.createQuery(JobVacancy.class);
        Root<JobVacancy> root = criteriaQuery.from(JobVacancy.class);

        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.isNotBlank(jobVacancySearchRequest.getJobType())) {
            predicates.add(criteriaBuilder.equal(root.get("jobType"), jobVacancySearchRequest.getJobType()));
        }

        if (jobVacancySearchRequest.getSalaryMin() != null) {
            if (!jobVacancySearchRequest.getCurrencyMin().equals("USD")) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(addAdjustedSalary(criteriaBuilder, root),
                        currencyService.convertFromBYNToUSD(jobVacancySearchRequest.getSalaryMin())));
            } else {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(addAdjustedSalary(criteriaBuilder, root),
                        jobVacancySearchRequest.getSalaryMin()));
            }
        }

        if (jobVacancySearchRequest.getSalaryMax() != null) {
            if (!jobVacancySearchRequest.getCurrencyMax().equals("USD")) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(addAdjustedSalary(criteriaBuilder, root),
                        currencyService.convertFromBYNToUSD(jobVacancySearchRequest.getSalaryMax())));
            } else {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(addAdjustedSalary(criteriaBuilder, root),
                        jobVacancySearchRequest.getSalaryMax()));
            }
        }

        if (jobVacancySearchRequest.getSkills() != null && !jobVacancySearchRequest.getNotEmptySkills().isEmpty()) {
            Join<JobVacancy, Skill> skillJoin = root.join("skills");
            List<String> skillNames = jobVacancySearchRequest.getSkillsNames();

            predicates.add(skillJoin.get("skill").in(skillNames));

            criteriaQuery.groupBy(root.get("id"));

            criteriaQuery.having(
                    criteriaBuilder.equal(
                            criteriaBuilder.countDistinct(skillJoin.get("skill")),
                            jobVacancySearchRequest.getSkills().size()
                    )
            );
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        TypedQuery<JobVacancy> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }
}

