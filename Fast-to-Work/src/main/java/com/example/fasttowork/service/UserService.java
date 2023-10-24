package com.example.fasttowork.service;

import com.example.fasttowork.entity.UserEntity;
import com.example.fasttowork.payload.request.RegistrationDto;

public interface UserService {
    void saveUser(RegistrationDto registrationDto);
    UserEntity findByEmail(String email);
    UserEntity findByUsername(String username);
}
