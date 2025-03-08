package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    @Test
    public void testFindAllTeachers() {
        // Given
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setLastName("Dupont");

        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher2.setLastName("Martin");

        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);
        when(teacherRepository.findAll()).thenReturn(teachers);

        // When
        List<Teacher> result = teacherService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Dupont", result.get(0).getLastName());
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    public void testFindById_TeacherExists() {
        // Given
        Long teacherId = 1L;
        Teacher teacher = new Teacher();
        teacher.setId(teacherId);
        teacher.setLastName("Dupont");

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));

        // When
        Teacher result = teacherService.findById(teacherId);

        // Then
        assertNotNull(result);
        assertEquals(teacherId, result.getId());
        assertEquals("Dupont", result.getLastName());
        verify(teacherRepository, times(1)).findById(teacherId);
    }

    @Test
    public void testFindById_TeacherNotFound() {
        // Given
        Long teacherId = 1L;
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        // When
        Teacher result = teacherService.findById(teacherId);

        // Then
        assertNull(result);
        verify(teacherRepository, times(1)).findById(teacherId);
    }
}
