package com.example.fasttowork.repository;

import com.example.fasttowork.entity.Resume;
import com.example.fasttowork.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> findByEmployeeId(Long userId);
}
