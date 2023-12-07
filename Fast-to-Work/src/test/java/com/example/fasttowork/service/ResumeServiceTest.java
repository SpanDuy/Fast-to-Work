package com.example.fasttowork.service;

import com.example.fasttowork.entity.*;
import com.example.fasttowork.exception.BadRequestException;
import com.example.fasttowork.mapper.JobVacancyMapper;
import com.example.fasttowork.mapper.ResumeMapper;
import com.example.fasttowork.payload.request.JobVacancyRequest;
import com.example.fasttowork.payload.request.ResumeRequest;
import com.example.fasttowork.payload.response.JobVacancyResponse;
import com.example.fasttowork.payload.response.ResumeResponse;
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
    @Mock
    private ResumeRepository resumeRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private ResumeServiceImpl resumeService;

    @Test
    void getAllResume_shouldReturnListOfResumes() {
        Resume resume1 = new Resume();
        Resume resume2 = new Resume();
        List<Resume> mockResumes = Arrays.asList(resume1, resume2);

        when(resumeRepository.findAll()).thenReturn(mockResumes);

        List<Resume> result = resumeService.getAllResumes();

        assertEquals(2, result.size());
        assertEquals(resume1, result.get(0));
        assertEquals(resume2, result.get(1));
    }

    @Test
    void testFindAllJobVacancy() {
        Employee employee = new Employee();
        employee.setId(1L);

        Resume resume1 = new Resume();
        resume1.setId(1L);
        resume1.setJobType("Resume 1");
        resume1.setEmployee(employee);

        Resume resume2 = new Resume();
        resume2.setId(2L);
        resume2.setJobType("Resume 2");
        resume2.setEmployee(employee);

        List<Resume> expectedVacancies = Arrays.asList(resume1, resume2);

        when(resumeRepository.findByEmployeeId(employee.getId())).thenReturn(expectedVacancies);

        List<Resume> resultResumes = resumeService.findAllResumes(employee);

        assertEquals(expectedVacancies, resultResumes);

        verify(resumeRepository, times(1)).findByEmployeeId(employee.getId());
    }

    @Test
    void testFindResumeById() {
        Employee employee = new Employee();
        employee.setId(1L);

        Resume resume = new Resume();
        resume.setId(1L);
        resume.setEmployee(employee);

        ResumeResponse expectedResponse = ResumeMapper.mapToResumeResponse(resume);

        when(resumeRepository.findById(resume.getId())).thenReturn(Optional.of(resume));

        ResumeResponse resultResponse = resumeService.findResumeById(resume.getId(), employee);

        assertEquals(expectedResponse, resultResponse);

        verify(resumeRepository, times(1)).findById(resume.getId());

        verifyNoMoreInteractions(resumeRepository);
    }

    @Test
    void testFindResumeByIdNotFound() {
        Employee employee = new Employee();
        employee.setId(1L);

        when(resumeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class,
                () -> resumeService.findResumeById(1L, employee));

        verify(resumeRepository, times(1)).findById(1L);

        verifyNoMoreInteractions(resumeRepository);
    }

    @Test
    void testFindResumeByIdWrongEmployee() {
        Employee employee = new Employee();
        employee.setId(1L);

        Resume resume = new Resume();
        resume.setId(1L);
        resume.setEmployee(new Employee());

        when(resumeRepository.findById(resume.getId())).thenReturn(Optional.of(resume));

        assertThrows(BadRequestException.class,
                () -> resumeService.findResumeById(resume.getId(), employee));

        verify(resumeRepository, times(1)).findById(resume.getId());

        verifyNoMoreInteractions(resumeRepository);
    }

    @Test
    void testGetResumeById() {
        Employee employee = new Employee();
        Resume resume = Resume.builder().employee(employee).build();
        resume.setId(1L);

        ResumeResponse expectedResponse = ResumeMapper.mapToResumeResponse(resume);

        when(resumeRepository.findById(resume.getId())).thenReturn(Optional.of(resume));

        ResumeResponse resultResponse = resumeService.getResumeById(resume.getId());

        assertEquals(expectedResponse, resultResponse);

        verify(resumeRepository, times(1)).findById(resume.getId());

        verifyNoMoreInteractions(resumeRepository);
    }

    @Test
    void testGetResumeByIdNotFound() {
        when(resumeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> resumeService.getResumeById(1L));

        verify(resumeRepository, times(1)).findById(1L);

        verifyNoMoreInteractions(resumeRepository);
    }

    @Test
    void testCreateResume() {
        ResumeRequest resumeRequest = ResumeRequest.builder().skills(new ArrayList<>()).build();
        Employee employee = new Employee();

        when(employeeRepository.save(any(Employee.class))).thenReturn(new Employee());

        Resume createdResume = resumeService.createResume(resumeRequest, employee);

        assertNotNull(createdResume);

        verify(employeeRepository, times(1)).save(any(Employee.class));
        verify(resumeRepository, times(1)).save(any(Resume.class));
    }

    @Test
    void testEditResume_ValidData_ShouldEditAndSaveResume() {
        Long id = 1L;
        Employee employee = new Employee();
        ResumeRequest resumeRequest = ResumeRequest.builder().skills(new ArrayList<>()).build();
        Resume existingResume = Resume.builder().id(id).employee(employee).build();

        when(resumeRepository.findById(id)).thenReturn(Optional.of(existingResume));

        assertDoesNotThrow(() -> resumeService.editResume(resumeRequest, id, employee));

        verify(resumeRepository, times(1)).findById(id);
        verify(resumeRepository, times(1)).save(any(Resume.class));
    }

    @Test
    void testEditResume_InvalidEmployee_ShouldThrowBadRequestException() {
        Long id = 1L;
        Employee employee = new Employee();
        ResumeRequest resumeRequest = ResumeRequest.builder().build();
        Resume existingResume = Resume.builder().id(id).employee(new Employee()).build();

        when(resumeRepository.findById(id)).thenReturn(Optional.of(existingResume));

        assertThrows(BadRequestException.class,
                () -> resumeService.editResume(resumeRequest, id, employee));

        verify(resumeRepository, times(1)).findById(id);
        verify(resumeRepository, never()).save(any(Resume.class));
    }

    @Test
    void testDeleteResume() {
        Long id = 1L;
        Employee employee = new Employee();
        Resume existingResume = new Resume();
        existingResume.setId(id);
        existingResume.setEmployee(employee);

        when(resumeRepository.findById(id)).thenReturn(java.util.Optional.of(existingResume));

        resumeService.deleteResume(id, employee);

        verify(resumeRepository, times(1)).deleteById(id);

        verify(resumeRepository, times(1)).findById(id);

        verifyNoMoreInteractions(resumeRepository);
    }

    @Test
    void testDeleteResumeThrowsExceptionWhenNotBelongToUser() {
        Long id = 1L;
        Employee employee = new Employee();
        Resume existingResume = new Resume();
        existingResume.setId(id);
        existingResume.setEmployee(new Employee());

        when(resumeRepository.findById(eq(id))).thenReturn(java.util.Optional.of(existingResume));

        assertThrows(BadRequestException.class, () -> resumeService.deleteResume(id, employee));

        verify(resumeRepository, times(1)).findById(eq(id));

        verifyNoMoreInteractions(resumeRepository);
    }
}
