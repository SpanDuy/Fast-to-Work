package com.example.fasttowork.repository;

import com.example.fasttowork.entity.Employee;
import com.example.fasttowork.entity.Employer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployerRepository  extends JpaRepository<Employer, Long> {
    Employer findByEmail(String email);
    Employer findByUsername(String userName);
    Employer findFirstByUsername(String username);
}
