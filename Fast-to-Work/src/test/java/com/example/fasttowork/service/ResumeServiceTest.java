package com.example.fasttowork.service;

import com.example.fasttowork.entity.*;
import com.example.fasttowork.exception.BadRequestException;
import com.example.fasttowork.payload.request.JobVacancyRequest;
import com.example.fasttowork.payload.request.ResumeRequest;
import com.example.fasttowork.repository.EmployeeRepository;
import com.example.fasttowork.repository.EmployerRepository;
import com.example.fasttowork.repository.JobVacancyRepository;
import com.example.fasttowork.repository.ResumeRepository;
import com.example.fasttowork.security.SecurityUtil;
import com.example.fasttowork.service.serviceImpl.JobVacancyServiceImpl;
import com.example.fasttowork.service.serviceImpl.ResumeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ResumeServiceTest {
    @MockBean
    private ResumeRepository resumeRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private SecurityUtil securityUtil;

    @InjectMocks
    private ResumeServiceImpl resumeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllResume_shouldReturnListOfResumes() {
        Resume resume1 = new Resume();
        Resume resume2 = new Resume();

        List<Resume> mockResumes = Arrays.asList(resume1, resume2);

        when(resumeRepository.findAll()).thenReturn(mockResumes);

        // Act
        List<Resume> result = resumeService.getAllResumes();

        // Assert
        assertEquals(2, result.size()); // Проверка, что возвращается ожидаемое количество элементов
        assertEquals(resume1, result.get(0)); // Проверка, что первый элемент соответствует ожидаемому объекту
        assertEquals(resume2, result.get(1)); // Проверка, что второй элемент соответствует ожидаемому объекту

        // Можете добавить дополнительные проверки в зависимости от вашей логики
    }

    @Test
    @WithMockUser(username = "test@example.com", password = "password", roles = "EMPLOYER")
    public void testFindAllResume() {
        when(securityUtil.getSessionUserEmail()).thenReturn("test@example.com");

        Employee user = new Employee();
        user.setId(1L); // Установите ID для пользователя
        when(employeeRepository.findByEmail(anyString())).thenReturn(user);

        List<Resume> expectedJobVacancies = new ArrayList<>();
        when(resumeRepository.findByEmployeeId(anyLong())).thenReturn(expectedJobVacancies);

        List<Resume> actualJobVacancies = resumeService.findAllResumes();

        verify(securityUtil, times(1)).getSessionUserEmail();
        verify(employeeRepository, times(1)).findByEmail("test@example.com");
        verify(resumeRepository, times(1)).findByEmployeeId(1L);

        assertEquals(expectedJobVacancies, actualJobVacancies);
    }

    @Test
    public void testFindResumeById_WhenExists() {
        Long resumeId = 1L;
        Resume expectedResume = new Resume();
        when(resumeRepository.findById(resumeId)).thenReturn(Optional.of(expectedResume));

        // Call the method you want to test
        Resume actualJobVacancy = resumeService.findResumeById(resumeId);

        // Verify that the method was called with the expected argument
        verify(resumeRepository, times(1)).findById(resumeId);

        // Verify the result
        assertEquals(expectedResume, actualJobVacancy);
    }

    @Test
    public void testFindResumeById_WhenNotExists() {
        // Mocking jobVacancyRepository.findById()
        Long resumeId = 1L;
        when(resumeRepository.findById(resumeId)).thenReturn(Optional.empty());

        // Call the method you want to test and expect an exception
        assertThrows(BadRequestException.class, () -> {
            resumeService.findResumeById(resumeId);
        });

        // Verify that the method was called with the expected argument
        verify(resumeRepository, times(1)).findById(resumeId);
    }

    @Test
    public void testCreateResume() {
        // Создаем тестовые данные
        ResumeRequest request = new ResumeRequest();
        request.setJobType("Full Time");
        request.setDescription("Job description");
        List<Skill> skills = new ArrayList<>();
        skills.add(Skill.builder().skill("Java").build());
        request.setSkills(skills);

        Long userId = 1L;

        Employee employee = new Employee();
        employee.setId(userId);
        employee.setEmail("test@example.com");

        when(securityUtil.getSessionUserEmail()).thenReturn("test@example.com");
        when(employeeRepository.findByEmail("test@example.com")).thenReturn(employee);

        // Вызываем тестируемый метод
        Resume createdResume = resumeService.createResume(request, userId);

        // Проверяем результаты
        assertNotNull(createdResume);
        assertEquals(request.getJobType(), createdResume.getJobType());
        assertEquals(request.getDescription(), createdResume.getDescription());
        assertEquals(employee, createdResume.getEmployee());
        assertEquals(skills, createdResume.getSkills());

        // Проверяем взаимодействие с репозиториями
        verify(resumeRepository, times(1)).save(createdResume);
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    public void testEditResumeNotFoundException() {
        // Попытка редактирования несуществующей вакансии
        Long nonExistingVacancyId = 2L;
        when(resumeRepository.findById(nonExistingVacancyId)).thenReturn(Optional.empty());

        // Вызываем тестируемый метод и ожидаем BadRequestException
        assertThrows(BadRequestException.class, () -> resumeService.editResume(new ResumeRequest(), nonExistingVacancyId));

        // Проверяем взаимодействие с репозиторием
        verify(resumeRepository, times(1)).findById(nonExistingVacancyId);
        verify(resumeRepository, never()).save(any());
    }

    @Test
    public void testDeleteResume() {
        // Подготовка данных для теста
        Long userId = 123L;
        Long resumeId = 456L;

        // Вызываем метод, который мы тестируем
        resumeService.deleteResume(userId, resumeId);

        // Проверяем, что метод deleteById был вызван с правильными аргументами
        verify(resumeRepository, times(1)).deleteById(resumeId);
    }
}
