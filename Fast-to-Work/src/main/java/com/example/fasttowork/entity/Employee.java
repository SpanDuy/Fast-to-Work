package com.example.fasttowork.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee")
public class Employee extends UserEntity {
    private String name;
    private String surname;
    private String middleName;
    private Date birthday;
    private String city;
    private Boolean gender;
    private String phoneNumber;

    @OneToMany(mappedBy = "employee",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    private List<Resume> resumes = new ArrayList<>();
}
