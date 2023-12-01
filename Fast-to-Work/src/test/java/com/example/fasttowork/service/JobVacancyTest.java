package com.example.fasttowork.service;

import com.example.fasttowork.entity.Employer;
import com.example.fasttowork.entity.JobVacancy;
import com.example.fasttowork.entity.Skill;
import com.example.fasttowork.entity.UserEntity;
import com.example.fasttowork.exception.BadRequestException;
import com.example.fasttowork.payload.request.JobVacancyRequest;
import com.example.fasttowork.payload.request.JobVacancySearchRequest;
import com.example.fasttowork.repository.EmployerRepository;
import com.example.fasttowork.repository.JobVacancyRepository;
import com.example.fasttowork.security.SecurityUtil;
import com.example.fasttowork.service.JobVacancyService;
import com.example.fasttowork.service.serviceImpl.CurrencyServiceImpl;
import com.example.fasttowork.service.serviceImpl.JobVacancyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.mockito.Mockito.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class JobVacancyTest {
    @MockBean
    private JobVacancyRepository jobVacancyRepository;

    @Mock
    private EmployerRepository employerRepository;

    @Mock
    private SecurityUtil securityUtil;

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<JobVacancy> criteriaQuery;

    @Mock
    private Root<JobVacancy> root;

    @Mock
    private TypedQuery<JobVacancy> typedQuery;

    @Mock
    private CurrencyService currencyService;

    @Mock
    private Expression<Double> adjustedSalaryExpression;

    @InjectMocks
    private JobVacancyServiceImpl jobVacancyService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllJobVacancy_shouldReturnListOfJobVacancies() {
        JobVacancy jobVacancy1 = new JobVacancy();
        JobVacancy jobVacancy2 = new JobVacancy();

        List<JobVacancy> mockJobVacancies = Arrays.asList(jobVacancy1, jobVacancy2);

        when(jobVacancyRepository.findAll()).thenReturn(mockJobVacancies);

        // Act
        List<JobVacancy> result = jobVacancyService.getAllJobVacancy();

        // Assert
        assertEquals(2, result.size()); // Проверка, что возвращается ожидаемое количество элементов
        assertEquals(jobVacancy1, result.get(0)); // Проверка, что первый элемент соответствует ожидаемому объекту
        assertEquals(jobVacancy2, result.get(1)); // Проверка, что второй элемент соответствует ожидаемому объекту

        // Можете добавить дополнительные проверки в зависимости от вашей логики
    }

    @Test
    @WithMockUser(username = "test@example.com", password = "password", roles = "EMPLOYER")
    public void testFindAllJobVacancy() {
        when(securityUtil.getSessionUserEmail()).thenReturn("test@example.com");

        Employer user = new Employer();
        user.setId(1L); // Установите ID для пользователя
        when(employerRepository.findByEmail(anyString())).thenReturn(user);

        List<JobVacancy> expectedJobVacancies = new ArrayList<>();
        when(jobVacancyRepository.findByEmployerId(anyLong())).thenReturn(expectedJobVacancies);

        List<JobVacancy> actualJobVacancies = jobVacancyService.findAllJobVacancy();

        verify(securityUtil, times(1)).getSessionUserEmail();
        verify(employerRepository, times(1)).findByEmail("test@example.com");
        verify(jobVacancyRepository, times(1)).findByEmployerId(1L);

        assertEquals(expectedJobVacancies, actualJobVacancies);
    }

    @Test
    public void testFindJobVacancyById_WhenExists() {
        // Mocking jobVacancyRepository.findById()
        Long jobId = 1L;
        JobVacancy expectedJobVacancy = new JobVacancy();
        when(jobVacancyRepository.findById(jobId)).thenReturn(Optional.of(expectedJobVacancy));

        // Call the method you want to test
        JobVacancy actualJobVacancy = jobVacancyService.findJobVacancyById(jobId);

        // Verify that the method was called with the expected argument
        verify(jobVacancyRepository, times(1)).findById(jobId);

        // Verify the result
        assertEquals(expectedJobVacancy, actualJobVacancy);
    }

    @Test
    public void testFindJobVacancyById_WhenNotExists() {
        // Mocking jobVacancyRepository.findById()
        Long jobId = 1L;
        when(jobVacancyRepository.findById(jobId)).thenReturn(Optional.empty());

        // Call the method you want to test and expect an exception
        assertThrows(BadRequestException.class, () -> {
            jobVacancyService.findJobVacancyById(jobId);
        });

        // Verify that the method was called with the expected argument
        verify(jobVacancyRepository, times(1)).findById(jobId);
    }

    @Test
    public void testCreateJobVacancy() {
        // Создаем тестовые данные
        JobVacancyRequest request = new JobVacancyRequest();
        request.setJobType("Full Time");
        request.setSalary(50000);
        request.setCurrency("USD");
        request.setDescription("Job description");
        List<Skill> skills = new ArrayList<>();
        skills.add(Skill.builder().skill("Java").build());
        request.setSkills(skills);

        Long userId = 1L;

        Employer employer = new Employer();
        employer.setId(userId);
        employer.setEmail("test@example.com");
        employer.setCompany("company");

        when(securityUtil.getSessionUserEmail()).thenReturn("test@example.com");
        when(employerRepository.findByEmail("test@example.com")).thenReturn(employer);

        // Вызываем тестируемый метод
        JobVacancy createdJobVacancy = jobVacancyService.createJobVacancy(request, userId);

        // Проверяем результаты
        assertNotNull(createdJobVacancy);
        assertEquals(request.getJobType(), createdJobVacancy.getJobType());
        assertEquals(request.getSalary(), createdJobVacancy.getSalary());
        assertEquals(request.getCurrency(), createdJobVacancy.getCurrency());
        assertEquals(request.getDescription(), createdJobVacancy.getDescription());
        assertEquals(employer, createdJobVacancy.getEmployer());
        assertEquals(skills, createdJobVacancy.getSkills());

        // Проверяем взаимодействие с репозиториями
        verify(jobVacancyRepository, times(1)).save(createdJobVacancy);
        verify(employerRepository, times(1)).save(employer);
    }

    @Test
    public void testEditJobVacancy() {
        // Создаем тестовые данные
        JobVacancyRequest request = new JobVacancyRequest();
        request.setJobType("Full Time");
        request.setSalary(50000);
        request.setCurrency("USD");
        request.setDescription("Updated Job description");
        List<Skill> skills = new ArrayList<>();
        skills.add(Skill.builder().skill("Java").build());
        request.setSkills(skills);

        Long vacancyId = 1L;

        JobVacancy existingJobVacancy = new JobVacancy();
        existingJobVacancy.setId(vacancyId);

        when(jobVacancyRepository.findById(vacancyId)).thenReturn(Optional.of(existingJobVacancy));

        // Вызываем тестируемый метод
        assertDoesNotThrow(() -> jobVacancyService.editJobVacancy(request, vacancyId));

        // Проверяем, что данные вакансии были изменены
        assertEquals(request.getJobType(), existingJobVacancy.getJobType());
        assertEquals(request.getSalary(), existingJobVacancy.getSalary());
        assertEquals(request.getCurrency(), existingJobVacancy.getCurrency());
        assertEquals(request.getDescription(), existingJobVacancy.getDescription());
        assertEquals(skills, existingJobVacancy.getSkills());

        // Проверяем взаимодействие с репозиторием
        verify(jobVacancyRepository, times(1)).findById(vacancyId);
        verify(jobVacancyRepository, times(1)).save(existingJobVacancy);
    }

    @Test
    public void testEditJobVacancyNotFoundException() {
        // Попытка редактирования несуществующей вакансии
        Long nonExistingVacancyId = 2L;
        when(jobVacancyRepository.findById(nonExistingVacancyId)).thenReturn(Optional.empty());

        // Вызываем тестируемый метод и ожидаем BadRequestException
        assertThrows(BadRequestException.class, () -> jobVacancyService.editJobVacancy(new JobVacancyRequest(), nonExistingVacancyId));

        // Проверяем взаимодействие с репозиторием
        verify(jobVacancyRepository, times(1)).findById(nonExistingVacancyId);
        verify(jobVacancyRepository, never()).save(any());
    }

    @Test
    public void testDeleteJobVacancy() {
        // Подготовка данных для теста
        Long userId = 123L;
        Long vacancyId = 456L;

        // Вызываем метод, который мы тестируем
        jobVacancyService.deleteJobVacancy(userId, vacancyId);

        // Проверяем, что метод deleteById был вызван с правильными аргументами
        verify(jobVacancyRepository, times(1)).deleteById(vacancyId);
    }

//    @Test
//    public void testSearchJobVacancy() {
//        // Подготовка тестовых данных
//        JobVacancySearchRequest searchRequest = new JobVacancySearchRequest();
//        searchRequest.setJobType("Full Time");
//        searchRequest.setSalaryMin(50000);
//        searchRequest.setCurrencyMin("USD");
//        searchRequest.setSkills(Arrays.asList(
//                Skill.builder().skill("Java").build(),
//                Skill.builder().skill("Spring").build()));
//
//        List<JobVacancy> expectedResult = Arrays.asList(
//                new JobVacancy(/* заполните значения для вакансии 1 */),
//                new JobVacancy(/* заполните значения для вакансии 2 */));
//
//        when(currencyService.getCurrentOfficialRate()).thenReturn(3.0);
//
//        CriteriaBuilder.Case selectCaseMock = Mockito.mock(CriteriaBuilder.Case.class);
//
//        Mockito.when(criteriaBuilder.selectCase()).thenReturn(selectCaseMock);
//        Mockito.when(selectCaseMock.when(Mockito.any(), Mockito.any())).thenAnswer(invocation -> {
//            Object[] args = invocation.getArguments();
//            return args[0]; // Возвращаем первый аргумент (Expression<Double>)
//        });
//
//        // Установка макетов для объектов Criteria API
//        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
//        when(criteriaBuilder.createQuery(JobVacancy.class)).thenReturn(criteriaQuery);
//        when(criteriaQuery.from(JobVacancy.class)).thenReturn(root);
//        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
//
//        // Установка ожидаемого результата для макета TypedQuery
//        when(typedQuery.getResultList()).thenReturn(expectedResult);
//
//        // Вызов тестируемого метода
//        List<JobVacancy> result = jobVacancyService.searchJobVacancy(searchRequest);
//
//        // Проверки
//        assertEquals(expectedResult, result);
//
//        // Проверка взаимодействия с объектами Criteria API
//        verify(criteriaBuilder, times(1)).equal(any(Expression.class), any());
//        verify(criteriaBuilder, times(1)).greaterThanOrEqualTo(any(Expression.class), any(Expression.class));
//        verify(criteriaBuilder, times(1)).lessThanOrEqualTo(any(Expression.class), any(Expression.class));
//        verify(root, times(1)).join(eq("skills"));
//
//        // Проверка взаимодействия с макетом TypedQuery
//        verify(entityManager, times(1)).createQuery(criteriaQuery);
//    }
}
