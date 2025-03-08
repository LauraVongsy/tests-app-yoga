package com.openclassrooms.starterjwt.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherDtoTest {

    private Validator validator;
    private TeacherDto teacherDto;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        teacherDto = new TeacherDto(
                1L,
                "Smith",
                "Alice",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    public void testAllArgsConstructor() {
        TeacherDto dto = new TeacherDto(
                2L,
                "Doe",
                "John",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals("Doe", dto.getLastName());
        assertEquals("John", dto.getFirstName());
        assertNotNull(dto.getCreatedAt());
        assertNotNull(dto.getUpdatedAt());
    }

    @Test
    public void testNoArgsConstructor() {
        TeacherDto emptyDto = new TeacherDto();
        assertNotNull(emptyDto);

        // Ensure fields are default values
        assertNull(emptyDto.getId());
        assertNull(emptyDto.getLastName());
        assertNull(emptyDto.getFirstName());
        assertNull(emptyDto.getCreatedAt());
        assertNull(emptyDto.getUpdatedAt());
    }

    @Test
    public void testGettersAndSetters() {
        TeacherDto dto = new TeacherDto();
        dto.setId(3L);
        dto.setLastName("Brown");
        dto.setFirstName("Charlie");
        dto.setCreatedAt(LocalDateTime.of(2024, 3, 1, 10, 30));
        dto.setUpdatedAt(LocalDateTime.of(2024, 3, 5, 15, 45));

        assertEquals(3L, dto.getId());
        assertEquals("Brown", dto.getLastName());
        assertEquals("Charlie", dto.getFirstName());
        assertEquals(LocalDateTime.of(2024, 3, 1, 10, 30), dto.getCreatedAt());
        assertEquals(LocalDateTime.of(2024, 3, 5, 15, 45), dto.getUpdatedAt());
    }

    @Test
    public void testEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);

        TeacherDto teacher1 = new TeacherDto(1L, "Doe", "John", now, now);
        TeacherDto teacher2 = new TeacherDto(1L, "Doe", "John", now, now);

        assertEquals(teacher1, teacher2);
        assertEquals(teacher1.hashCode(), teacher2.hashCode());

        TeacherDto teacher3 = new TeacherDto(2L, "Smith", "Alice", now, now);
        assertNotEquals(teacher1, teacher3);
    }

    @Test
    public void testValidation_ValidTeacherDto_ShouldPass() {
        Set<ConstraintViolation<TeacherDto>> violations = validator.validate(teacherDto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testValidation_BlankLastName_ShouldFail() {
        teacherDto.setLastName("");

        Set<ConstraintViolation<TeacherDto>> violations = validator.validate(teacherDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testValidation_BlankFirstName_ShouldFail() {
        teacherDto.setFirstName("");

        Set<ConstraintViolation<TeacherDto>> violations = validator.validate(teacherDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testValidation_LongLastName_ShouldFail() {
        teacherDto.setLastName("a".repeat(21));

        Set<ConstraintViolation<TeacherDto>> violations = validator.validate(teacherDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testValidation_LongFirstName_ShouldFail() {
        teacherDto.setFirstName("a".repeat(21));

        Set<ConstraintViolation<TeacherDto>> violations = validator.validate(teacherDto);
        assertFalse(violations.isEmpty());
    }
}
