package com.example.fasttowork.payload.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegistrationEmployerDto {
    private String email;
    private String company;
}
