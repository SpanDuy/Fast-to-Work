package com.example.fasttowork.repository;

import com.example.fasttowork.entity.Employee;
import com.example.fasttowork.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByEmail(String email);
    Employee findByUsername(String userName);
    Employee findFirstByUsername(String username);
}
