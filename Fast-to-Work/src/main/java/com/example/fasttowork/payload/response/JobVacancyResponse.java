package com.example.fasttowork.payload.response;

import com.example.fasttowork.entity.Employer;
import com.example.fasttowork.entity.Skill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobVacancyResponse {
    private Long id;
    private String company;
    private String email;

    private String jobType;
    private Integer salary;
    private String currency;
    private String description;
    private List<Skill> skills;
}
