package com.example.fasttowork.payload.request;

import com.example.fasttowork.entity.Skill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

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
}
