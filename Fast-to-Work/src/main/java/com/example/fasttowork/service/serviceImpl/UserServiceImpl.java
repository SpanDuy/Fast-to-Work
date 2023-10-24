package com.example.fasttowork.service.serviceImpl;

import com.example.fasttowork.entity.UserEntity;
import com.example.fasttowork.payload.request.RegistrationDto;
import com.example.fasttowork.repository.RoleRepository;
import com.example.fasttowork.repository.UserRepository;
import com.example.fasttowork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.fasttowork.entity.Role;

import java.util.Arrays;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(RegistrationDto registrationDto) {
        UserEntity user = new UserEntity();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setName(registrationDto.getName());
        user.setSurname(registrationDto.getSurname());
        user.setMiddleName(registrationDto.getMiddleName());
        user.setBirthday(registrationDto.getBirthday());
        user.setCity(registrationDto.getCity());
        user.setGender(registrationDto.getGender());
        user.setPhoneNumber(registrationDto.getPhoneNumber());
        Role role = roleRepository.findByName("EMPLOYEE");
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
