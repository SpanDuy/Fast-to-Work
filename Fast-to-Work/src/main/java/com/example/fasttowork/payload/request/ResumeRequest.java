package com.example.fasttowork.payload.request;

import com.example.fasttowork.entity.Skill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeRequest {

    private Long id;
    private String jobType;
    private List<Skill> skills;
    private String description;

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
