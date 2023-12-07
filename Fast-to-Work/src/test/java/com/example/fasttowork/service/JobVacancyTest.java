package com.example.fasttowork.service;

import com.example.fasttowork.entity.Employer;
import com.example.fasttowork.entity.JobVacancy;
import com.example.fasttowork.entity.Skill;
import com.example.fasttowork.entity.UserEntity;
import com.example.fasttowork.exception.BadRequestException;
import com.example.fasttowork.mapper.JobVacancyMapper;
import com.example.fasttowork.payload.request.JobVacancyRequest;
import com.example.fasttowork.payload.request.JobVacancySearchRequest;
import com.example.fasttowork.payload.response.JobVacancyResponse;
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
    @Mock
    private JobVacancyRepository jobVacancyRepository;
    @Mock
    private EmployerRepository employerRepository;
    @InjectMocks
    private JobVacancyServiceImpl jobVacancyService;

    @Test
    void getAllJobVacancy_shouldReturnListOfJobVacancies() {
        JobVacancy jobVacancy1 = new JobVacancy();
        JobVacancy jobVacancy2 = new JobVacancy();
        List<JobVacancy> mockJobVacancies = Arrays.asList(jobVacancy1, jobVacancy2);

        when(jobVacancyRepository.findAll()).thenReturn(mockJobVacancies);

        List<JobVacancy> result = jobVacancyService.getAllJobVacancy();

        assertEquals(2, result.size());
        assertEquals(jobVacancy1, result.get(0));
        assertEquals(jobVacancy2, result.get(1));
    }

    @Test
    void testFindAllJobVacancy() {
        Employer employer = new Employer();
        employer.setId(1L);

        JobVacancy vacancy1 = new JobVacancy();
        vacancy1.setId(1L);
        vacancy1.setJobType("Vacancy 1");
        vacancy1.setEmployer(employer);

        JobVacancy vacancy2 = new JobVacancy();
        vacancy2.setId(2L);
        vacancy2.setJobType("Vacancy 2");
        vacancy2.setEmployer(employer);

        List<JobVacancy> expectedVacancies = Arrays.asList(vacancy1, vacancy2);

        when(jobVacancyRepository.findByEmployerId(employer.getId())).thenReturn(expectedVacancies);

        List<JobVacancy> resultVacancies = jobVacancyService.findAllJobVacancy(employer);

        assertEquals(expectedVacancies, resultVacancies);

        verify(jobVacancyRepository, times(1)).findByEmployerId(employer.getId());
    }

    @Test
    void testFindJobVacancyById() {
        Employer employer = new Employer();
        employer.setId(1L);

        JobVacancy jobVacancy = new JobVacancy();
        jobVacancy.setId(1L);
        jobVacancy.setEmployer(employer);

        JobVacancyResponse expectedResponse = JobVacancyMapper.mapToJobVacancyResponse(jobVacancy);

        when(jobVacancyRepository.findById(jobVacancy.getId())).thenReturn(Optional.of(jobVacancy));

        JobVacancyResponse resultResponse = jobVacancyService.findJobVacancyById(jobVacancy.getId(), employer);

        assertEquals(expectedResponse, resultResponse);

        verify(jobVacancyRepository, times(1)).findById(jobVacancy.getId());

        verifyNoMoreInteractions(jobVacancyRepository);
    }

    @Test
    void testFindJobVacancyByIdNotFound() {
        Employer employer = new Employer();
        employer.setId(1L);

        when(jobVacancyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class,
                () -> jobVacancyService.findJobVacancyById(1L, employer));

        verify(jobVacancyRepository, times(1)).findById(1L);

        verifyNoMoreInteractions(jobVacancyRepository);
    }

    @Test
    void testFindJobVacancyByIdWrongEmployer() {
        Employer employer = new Employer();
        employer.setId(1L);

        JobVacancy jobVacancy = new JobVacancy();
        jobVacancy.setId(1L);
        jobVacancy.setEmployer(new Employer());

        when(jobVacancyRepository.findById(jobVacancy.getId())).thenReturn(Optional.of(jobVacancy));

        assertThrows(BadRequestException.class,
                () -> jobVacancyService.findJobVacancyById(jobVacancy.getId(), employer));

        verify(jobVacancyRepository, times(1)).findById(jobVacancy.getId());

        verifyNoMoreInteractions(jobVacancyRepository);
    }

    @Test
    void testGetJobVacancyById() {
        Employer employer = new Employer();
        JobVacancy jobVacancy = JobVacancy.builder().employer(employer).build();
        jobVacancy.setId(1L);

        JobVacancyResponse expectedResponse = JobVacancyMapper.mapToJobVacancyResponse(jobVacancy);

        when(jobVacancyRepository.findById(jobVacancy.getId())).thenReturn(Optional.of(jobVacancy));

        JobVacancyResponse resultResponse = jobVacancyService.getJobVacancyById(jobVacancy.getId());

        assertEquals(expectedResponse, resultResponse);

        verify(jobVacancyRepository, times(1)).findById(jobVacancy.getId());

        verifyNoMoreInteractions(jobVacancyRepository);
    }

    @Test
    void testGetJobVacancyByIdNotFound() {
        when(jobVacancyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> jobVacancyService.getJobVacancyById(1L));

        verify(jobVacancyRepository, times(1)).findById(1L);

        verifyNoMoreInteractions(jobVacancyRepository);
    }

    @Test
    void testCreateJobVacancy() {
        JobVacancyRequest jobVacancyRequest = JobVacancyRequest.builder().skills(new ArrayList<>()).build();
        Employer employer = new Employer();

        when(employerRepository.save(any(Employer.class))).thenReturn(new Employer());

        JobVacancy createdJobVacancy = jobVacancyService.createJobVacancy(jobVacancyRequest, employer);

        assertNotNull(createdJobVacancy);

        verify(employerRepository, times(1)).save(any(Employer.class));
        verify(jobVacancyRepository, times(1)).save(any(JobVacancy.class));
    }

    @Test
    void testEditJobVacancy_ValidData_ShouldEditAndSaveJobVacancy() {
        Long id = 1L;
        Employer employer = new Employer();
        JobVacancyRequest jobVacancyRequest = JobVacancyRequest.builder().skills(new ArrayList<>()).build();
        JobVacancy existingJobVacancy = JobVacancy.builder().id(id).employer(employer).build();

        when(jobVacancyRepository.findById(id)).thenReturn(Optional.of(existingJobVacancy));

        assertDoesNotThrow(() -> jobVacancyService.editJobVacancy(jobVacancyRequest, id, employer));

        verify(jobVacancyRepository, times(1)).findById(id);
        verify(jobVacancyRepository, times(1)).save(any(JobVacancy.class));
    }

    @Test
    void testEditJobVacancy_InvalidEmployer_ShouldThrowBadRequestException() {
        Long id = 1L;
        Employer employer = new Employer();
        JobVacancyRequest jobVacancyRequest = JobVacancyRequest.builder().build();
        JobVacancy existingJobVacancy = JobVacancy.builder().id(id).employer(new Employer()).build();

        when(jobVacancyRepository.findById(id)).thenReturn(Optional.of(existingJobVacancy));

        assertThrows(BadRequestException.class,
                () -> jobVacancyService.editJobVacancy(jobVacancyRequest, id, employer));

        verify(jobVacancyRepository, times(1)).findById(id);
        verify(jobVacancyRepository, never()).save(any(JobVacancy.class));
    }

    @Test
    void testDeleteJobVacancy() {
        Long id = 1L;
        Employer employer = new Employer();
        JobVacancy existingJobVacancy = new JobVacancy();
        existingJobVacancy.setId(id);
        existingJobVacancy.setEmployer(employer);

        when(jobVacancyRepository.findById(id)).thenReturn(java.util.Optional.of(existingJobVacancy));

        jobVacancyService.deleteJobVacancy(id, employer);

        verify(jobVacancyRepository, times(1)).deleteById(id);

        verify(jobVacancyRepository, times(1)).findById(id);

        verifyNoMoreInteractions(jobVacancyRepository);
    }

    @Test
    void testDeleteJobVacancyThrowsExceptionWhenNotBelongToUser() {
        Long id = 1L;
        Employer employer = new Employer();
        JobVacancy existingJobVacancy = new JobVacancy();
        existingJobVacancy.setId(id);
        existingJobVacancy.setEmployer(new Employer());

        when(jobVacancyRepository.findById(eq(id))).thenReturn(java.util.Optional.of(existingJobVacancy));

        assertThrows(BadRequestException.class, () -> jobVacancyService.deleteJobVacancy(id, employer));

        verify(jobVacancyRepository, times(1)).findById(eq(id));

        verifyNoMoreInteractions(jobVacancyRepository);
    }
}
