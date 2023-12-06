package com.example.fasttowork.payload.request;

import com.example.fasttowork.entity.Skill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeSearchRequest {
    private String jobType;
    private Integer ageMin;
    private Integer ageMax;
    private String city;
    private List<Skill> skills;

    public List<Skill> getNotEmptySkills() {
        deleteNotEmptySkills();

        return skills;
    }

    public void deleteNotEmptySkills() {
        skills = skills.stream()
                .filter(item -> item != null && !item.getSkill().isEmpty())
                .collect(Collectors.toList());
    }

    public List<String> getSkillsNames() {
        return skills.stream()
                .map(Skill::getSkill)
                .collect(Collectors.toList());
    }
}
