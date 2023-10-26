package com.example.fasttowork.repository;

import com.example.fasttowork.entity.JobVacancy;
import com.example.fasttowork.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobVacancyRepository extends JpaRepository<JobVacancy, Long> {
    List<JobVacancy> findByEmployerId(Long userId);
}
