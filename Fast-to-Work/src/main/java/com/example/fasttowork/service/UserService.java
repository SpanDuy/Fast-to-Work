package com.example.fasttowork.service;

import com.example.fasttowork.entity.UserEntity;
import com.example.fasttowork.payload.request.RegistrationEmployeeDto;
import com.example.fasttowork.payload.request.RegistrationEmployerDto;
import com.example.fasttowork.payload.request.RegistrationUserDto;

public interface UserService {
    UserEntity saveUser(RegistrationUserDto registrationDto);
    void saveEmployee(RegistrationEmployeeDto registrationEmployeeDto);
    void saveEmployer(RegistrationEmployerDto registrationEmployerDto);
    UserEntity findByEmail(String email);
    UserEntity findByUsername(String username);
    UserEntity getCurrentUser();
}
