package com.example.fasttowork.payload.response;

import com.example.fasttowork.entity.Skill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeResponse {
    private Long id;
    private String email;

    private String name;
    private String surname;
    private String middleName;
    private LocalDateTime birthday;
    private String city;
    private Boolean gender;
    private String phoneNumber;

    private String jobType;
    private List<Skill> skills;
    private String description;
}
