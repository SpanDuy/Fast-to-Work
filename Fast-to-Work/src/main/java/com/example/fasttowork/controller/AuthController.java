package com.example.fasttowork.controller;

import com.example.fasttowork.entity.Employee;
import com.example.fasttowork.entity.Role;
import com.example.fasttowork.entity.UserEntity;
import com.example.fasttowork.payload.request.RegistrationEmployeeDto;
import com.example.fasttowork.payload.request.RegistrationEmployerDto;
import com.example.fasttowork.payload.request.RegistrationUserDto;
import com.example.fasttowork.security.SecurityUtil;
import com.example.fasttowork.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
public class AuthController {
    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/register")
    public String getRegisterForm(Model model) {
        RegistrationUserDto user = new RegistrationUserDto();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register/save")
    public String register(@Valid @ModelAttribute("user") RegistrationUserDto user,
                           BindingResult result, Model model) {
        UserEntity existingUserEmail = userService.findByEmail(user.getEmail());
        if(existingUserEmail != null && existingUserEmail.getEmail() != null && !existingUserEmail.getEmail().isEmpty()) {
            return "redirect:/register?fail";
        }
        UserEntity existingUserUsername = userService.findByUsername(user.getUsername());
        if(existingUserUsername != null && existingUserUsername.getUsername() != null && !existingUserUsername.getUsername().isEmpty()) {
            return "redirect:/register?fail";
        }
        if(result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        UserEntity registeredUser = userService.saveUser(user);

        if (user.getRole().getName().equals("EMPLOYEE")) {
            return "redirect:/register/employee/" + registeredUser.getEmail() + "?success";
        } else {
            return "redirect:/register/employer/" + registeredUser.getEmail() + "?success";
        }
    }

    @GetMapping("/register/employee/{email}")
    public String getEmployeeRegisterForm(@PathVariable("email") String email, Model model, HttpSession session) {
        RegistrationEmployeeDto employee = new RegistrationEmployeeDto();
        model.addAttribute("employee", employee);
        session.setAttribute("email", email);
        return "register-employee";
    }

    @PostMapping("/register/employee/save")
    public String employeeRegister(@Valid @ModelAttribute("employee") RegistrationEmployeeDto employee,
                                   BindingResult result,
                                   Model model,
                                   HttpSession session) {
        if(result.hasErrors()) {
            model.addAttribute("employee", employee);
            return "register-employee";
        }
        employee.setEmail((String) session.getAttribute("email"));
        session.removeAttribute("email");
        userService.saveEmployee(employee);
        return "redirect:/login?success";
    }

    @GetMapping("/register/employer/{email}")
    public String getEmployerRegisterForm(@PathVariable("email") String email, Model model, HttpSession session) {
        RegistrationEmployerDto employer = new RegistrationEmployerDto();
        model.addAttribute("employer", employer);
        session.setAttribute("email", email);
        return "register-employer";
    }

    @PostMapping("/register/employer/save")
    public String employerRegister(@Valid @ModelAttribute("employer") RegistrationEmployerDto employer,
                                   BindingResult result,
                                   Model model,
                                   HttpSession session) {
        if(result.hasErrors()) {
            model.addAttribute("employer", employer);
            return "register-employer";
        }
        employer.setEmail((String) session.getAttribute("email"));
        session.removeAttribute("email");
        userService.saveEmployer(employer);
        return "redirect:/login?success";
    }

    @GetMapping("/")
    public String redirect() {
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByEmail(username);

        List<Role> roles = user.getRoles();
        boolean isEmployee = roles.stream()
                .anyMatch(role -> role.getName().equals("EMPLOYEE"));

        if (isEmployee) {
            return "redirect:/Resume?success";
        } else {
            return "redirect:/job-vacancy?success";
        }
    }
}
