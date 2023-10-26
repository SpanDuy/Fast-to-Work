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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeRequest {

    private Long id;
    private String jobType;
//    private String name;
//    private String surname;
//    private String middleName;
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//    private Date birthday;
//    private String city;
//    private Boolean gender;
//    private String phoneNumber;
    private List<Skill> skills;
    private String gitLink;
}
