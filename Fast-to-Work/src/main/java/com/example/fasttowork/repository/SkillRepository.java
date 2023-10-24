package com.example.fasttowork.repository;

import com.example.fasttowork.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SkillRepository extends JpaRepository<Skill, Long> {
}
