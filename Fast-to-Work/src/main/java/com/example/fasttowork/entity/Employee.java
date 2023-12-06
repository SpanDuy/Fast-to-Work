package com.example.fasttowork.entity;

import lombok.*;

import javax.persistence.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee")
@ToString
public class Employee extends UserEntity {
    private String name;
    private String surname;
    private String middleName;
    private LocalDateTime birthday;
    private String city;
    private Boolean gender;
    private String phoneNumber;

    @OneToMany(mappedBy = "employee",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    @ToString.Exclude
    private List<Resume> resumes = new ArrayList<>();
}
