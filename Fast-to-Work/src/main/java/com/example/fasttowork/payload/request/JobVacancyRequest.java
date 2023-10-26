package com.example.fasttowork.payload.request;

import com.example.fasttowork.entity.Skill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Currency;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobVacancyRequest {
    private Long id;
    private String jobType;
    private Integer salary;
    private String description;
    private List<Skill> skills;
}
