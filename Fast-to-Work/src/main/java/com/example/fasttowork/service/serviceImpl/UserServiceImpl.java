package com.example.fasttowork.service.serviceImpl;

import com.example.fasttowork.entity.Employee;
import com.example.fasttowork.entity.Employer;
import com.example.fasttowork.entity.UserEntity;
import com.example.fasttowork.payload.request.RegistrationEmployeeDto;
import com.example.fasttowork.payload.request.RegistrationEmployerDto;
import com.example.fasttowork.payload.request.RegistrationUserDto;
import com.example.fasttowork.repository.EmployeeRepository;
import com.example.fasttowork.repository.EmployerRepository;
import com.example.fasttowork.repository.RoleRepository;
import com.example.fasttowork.repository.UserRepository;
import com.example.fasttowork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.fasttowork.entity.Role;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private EmployeeRepository employeeRepository;
    private EmployerRepository employerRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           EmployeeRepository employeeRepository,
                           EmployerRepository employerRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.employeeRepository = employeeRepository;
        this.employerRepository = employerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserEntity saveUser(RegistrationUserDto registrationUserDto) {
        UserEntity user;
        if (registrationUserDto.getRole().getName().equals("EMPLOYEE")) {
            user = new Employee();
        } else {
            user = new Employer();
        }
        // UserEntity user = new UserEntity();
        user.setUsername(registrationUserDto.getUsername());
        user.setEmail(registrationUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));

        Role role = roleRepository.findByName(registrationUserDto.getRole().getName());
        user.setRoles(Arrays.asList(role));

        if (registrationUserDto.getRole().getName().equals("EMPLOYEE")) {
            employeeRepository.save((Employee) user);
        } else {
            employerRepository.save((Employer) user);
        }

        return user;
    }

    @Override
    public void saveEmployee(RegistrationEmployeeDto registrationDto) {
        Employee employee = (Employee) userRepository.findByEmail(registrationDto.getEmail());

        employee.setName(registrationDto.getName());
        employee.setSurname(registrationDto.getSurname());
        employee.setMiddleName(registrationDto.getMiddleName());
        employee.setBirthday(Instant.ofEpochMilli(registrationDto.getBirthday().getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime());
        employee.setCity(registrationDto.getCity());
        employee.setGender(registrationDto.getGender());
        employee.setPhoneNumber(registrationDto.getPhoneNumber());

        userRepository.save(employee);
    }

    @Override
    public void saveEmployer(RegistrationEmployerDto registrationEmployerDto) {
        Employer employer = (Employer) userRepository.findByEmail(registrationEmployerDto.getEmail());

        employer.setCompany(registrationEmployerDto.getCompany());

        userRepository.save(employer);
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
