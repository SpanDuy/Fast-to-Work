package com.example.fasttowork.payload.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.example.fasttowork.entity.Resume;
import com.example.fasttowork.entity.Role;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class RegistrationUserDto {
    private Long id;
    @NotEmpty
    private String username;
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;

    @NotNull
    private Role role;
}
