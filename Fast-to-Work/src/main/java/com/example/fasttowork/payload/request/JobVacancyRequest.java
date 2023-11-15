package com.example.fasttowork.payload.request;

import com.example.fasttowork.entity.Skill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.Currency;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobVacancyRequest {
    private Long id;
    @NotEmpty(message = "Job Type should not be empty")
    private String jobType;
    @NotEmpty
    private Integer salary;
    @NotEmpty
    private String currency;
    @NotEmpty
    private String description;
    private List<Skill> skills;
}
