package com.example.fasttowork.payload.request;

import com.example.fasttowork.entity.Skill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobVacancySearchRequest {
    private String jobType;
    private Integer salaryMin;
    private Integer salaryMax;
    private String currency;
    private List<Skill> skills;
}
