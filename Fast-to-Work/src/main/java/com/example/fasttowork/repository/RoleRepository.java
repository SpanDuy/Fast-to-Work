package com.example.fasttowork.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fasttowork.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}