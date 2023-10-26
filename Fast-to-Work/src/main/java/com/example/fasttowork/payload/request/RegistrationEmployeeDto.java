package com.example.fasttowork.payload.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
public class RegistrationEmployeeDto {
    private String email;
    @NotEmpty
    private String name;
    @NotEmpty
    private String surname;
    @NotEmpty
    private String middleName;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date birthday;
    @NotEmpty
    private String city;
    @NotNull
    private Boolean gender;
    @NotEmpty
    private String phoneNumber;
}
