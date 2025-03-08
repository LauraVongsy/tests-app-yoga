package com.openclassrooms.starterjwt.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SessionDtoTest {

    private Validator validator;
    private SessionDto sessionDto;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        sessionDto = new SessionDto(
                1L,
                "Math Session",
                new Date(),
                10L,
                "This is an advanced math session.",
                Arrays.asList(100L, 101L),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    public void testAllArgsConstructor() {
        SessionDto dto = new SessionDto(
                2L,
                "Science Class",
                new Date(),
                20L,
                "Physics fundamentals course.",
                Arrays.asList(200L, 201L),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals("Science Class", dto.getName());
        assertNotNull(dto.getDate());
        assertEquals(20L, dto.getTeacher_id());
        assertEquals("Physics fundamentals course.", dto.getDescription());
        assertNotNull(dto.getUsers());
        assertEquals(2, dto.getUsers().size());
        assertNotNull(dto.getCreatedAt());
        assertNotNull(dto.getUpdatedAt());
    }

    @Test
    public void testNoArgsConstructor() {
        SessionDto emptyDto = new SessionDto();
        assertNotNull(emptyDto);

        // Ensure fields are default values
        assertNull(emptyDto.getId());
        assertNull(emptyDto.getName());
        assertNull(emptyDto.getDate());
        assertNull(emptyDto.getTeacher_id());
        assertNull(emptyDto.getDescription());
        assertNull(emptyDto.getUsers());
        assertNull(emptyDto.getCreatedAt());
        assertNull(emptyDto.getUpdatedAt());
    }

    @Test
    public void testGettersAndSetters() {
        SessionDto dto = new SessionDto();
        dto.setId(3L);
        dto.setName("History Session");
        dto.setDate(new Date());
        dto.setTeacher_id(30L);
        dto.setDescription("A deep dive into world history.");
        dto.setUsers(Arrays.asList(300L, 301L));
        dto.setCreatedAt(LocalDateTime.of(2024, 3, 1, 10, 30));
        dto.setUpdatedAt(LocalDateTime.of(2024, 3, 5, 15, 45));

        assertEquals(3L, dto.getId());
        assertEquals("History Session", dto.getName());
        assertNotNull(dto.getDate());
        assertEquals(30L, dto.getTeacher_id());
        assertEquals("A deep dive into world history.", dto.getDescription());
        assertEquals(2, dto.getUsers().size());
        assertEquals(LocalDateTime.of(2024, 3, 1, 10, 30), dto.getCreatedAt());
        assertEquals(LocalDateTime.of(2024, 3, 5, 15, 45), dto.getUpdatedAt());
    }

    @Test
    public void testEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);

        SessionDto session1 = new SessionDto(1L, "Session A", new Date(), 10L, "Desc A", Arrays.asList(100L, 101L), now, now);
        SessionDto session2 = new SessionDto(1L, "Session A", new Date(), 10L, "Desc A", Arrays.asList(100L, 101L), now, now);

        assertEquals(session1, session2);
        assertEquals(session1.hashCode(), session2.hashCode());

        SessionDto session3 = new SessionDto(2L, "Session B", new Date(), 20L, "Desc B", Arrays.asList(200L, 201L), now, now);
        assertNotEquals(session1, session3);
    }

    @Test
    public void testValidation_ValidSessionDto_ShouldPass() {
        Set<ConstraintViolation<SessionDto>> violations = validator.validate(sessionDto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testValidation_BlankName_ShouldFail() {
        sessionDto.setName("");

        Set<ConstraintViolation<SessionDto>> violations = validator.validate(sessionDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testValidation_NullDate_ShouldFail() {
        sessionDto.setDate(null);

        Set<ConstraintViolation<SessionDto>> violations = validator.validate(sessionDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testValidation_NullTeacherId_ShouldFail() {
        sessionDto.setTeacher_id(null);

        Set<ConstraintViolation<SessionDto>> violations = validator.validate(sessionDto);
        assertFalse(violations.isEmpty());
    }


    @Test
    public void testValidation_LongName_ShouldFail() {
        sessionDto.setName("a".repeat(51));

        Set<ConstraintViolation<SessionDto>> violations = validator.validate(sessionDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testValidation_LongDescription_ShouldFail() {
        sessionDto.setDescription("a".repeat(2501));

        Set<ConstraintViolation<SessionDto>> violations = validator.validate(sessionDto);
        assertFalse(violations.isEmpty());
    }
}
