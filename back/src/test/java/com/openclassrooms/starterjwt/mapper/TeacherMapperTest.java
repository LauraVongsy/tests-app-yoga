package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherMapperTest {

    private final TeacherMapper teacherMapper = Mappers.getMapper(TeacherMapper.class);
    private Teacher teacher;
    private TeacherDto teacherDto;

    @BeforeEach
    void setUp() {
        teacher = Teacher.builder()
                .id(1L)
                .firstName("Alice")
                .lastName("Smith")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        teacherDto = new TeacherDto(
                1L,
                "Smith",
                "Alice",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    public void testTeacherToTeacherDto() {
        // When
        TeacherDto mappedDto = teacherMapper.toDto(teacher);

        // Then
        assertNotNull(mappedDto);
        assertEquals(teacher.getId(), mappedDto.getId());
        assertEquals(teacher.getFirstName(), mappedDto.getFirstName());
        assertEquals(teacher.getLastName(), mappedDto.getLastName());
        assertEquals(teacher.getCreatedAt(), mappedDto.getCreatedAt());
        assertEquals(teacher.getUpdatedAt(), mappedDto.getUpdatedAt());
    }

    @Test
    public void testTeacherDtoToTeacher() {
        // When
        Teacher mappedTeacher = teacherMapper.toEntity(teacherDto);

        // Then
        assertNotNull(mappedTeacher);
        assertEquals(teacherDto.getId(), mappedTeacher.getId());
        assertEquals(teacherDto.getFirstName(), mappedTeacher.getFirstName());
        assertEquals(teacherDto.getLastName(), mappedTeacher.getLastName());
        assertEquals(teacherDto.getCreatedAt(), mappedTeacher.getCreatedAt());
        assertEquals(teacherDto.getUpdatedAt(), mappedTeacher.getUpdatedAt());
    }

    @Test
    public void testTeacherListToTeacherDtoList() {
        // Given
        Teacher anotherTeacher = Teacher.builder()
                .id(2L)
                .firstName("Bob")
                .lastName("Johnson")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<Teacher> teachers = Arrays.asList(teacher, anotherTeacher);

        // When
        List<TeacherDto> teacherDto = teacherMapper.toDto(teachers);

        // Then
        assertNotNull(teacherDto);
        assertEquals(2, teacherDto.size());
        assertEquals(teacher.getFirstName(), teacherDto.get(0).getFirstName());
        assertEquals(anotherTeacher.getFirstName(), teacherDto.get(1).getFirstName());
    }

    @Test
    public void testTeacherDtoListToTeacherList() {
        // Given
        TeacherDto anotherTeacherDto = new TeacherDto(
                2L,
                "Johnson",
                "Bob",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        List<TeacherDto> teacherDtoList = Arrays.asList(teacherDto, anotherTeacherDto);

        // When
        List<Teacher> teacherList = teacherMapper.toEntity(teacherDtoList);

        // Then
        assertNotNull(teacherList);
        assertEquals(2, teacherList.size());
        assertEquals(teacherDto.getFirstName(), teacherList.get(0).getFirstName());
        assertEquals(anotherTeacherDto.getFirstName(), teacherList.get(1).getFirstName());
    }

    @Test
    public void testNullTeacherToTeacherDto() {
        assertNull(teacherMapper.toDto((List<Teacher>) null));
    }

    @Test
    public void testNullTeacherDtoToTeacher() {
        assertNull(teacherMapper.toEntity((List<TeacherDto>) null));
    }

    @Test
    public void testNullTeacherListToTeacherDtoList() {
        assertNull(teacherMapper.toDto((List<Teacher>) null));
    }

    @Test
    public void testNullTeacherDtoListToTeacherList() {
        assertNull(teacherMapper.toEntity((List<TeacherDto>) null));
    }
}
