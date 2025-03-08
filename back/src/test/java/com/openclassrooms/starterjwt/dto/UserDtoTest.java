package com.openclassrooms.starterjwt.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserDtoTest {

    private Validator validator;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        userDto = new UserDto(
                1L,
                "test@example.com",
                "Doe",
                "John",
                true,
                "securePassword123",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    public void testAllArgsConstructor() {
        UserDto dto = new UserDto(
                2L,
                "john.doe@example.com",
                "Doe",
                "John",
                false,
                "password123",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals("john.doe@example.com", dto.getEmail());
        assertEquals("Doe", dto.getLastName());
        assertEquals("John", dto.getFirstName());
        assertFalse(dto.isAdmin());
        assertEquals("password123", dto.getPassword());
    }

    @Test
    public void testNoArgsConstructor() {
        UserDto emptyDto = new UserDto();
        assertNotNull(emptyDto);

        // Ensure fields are default values
        assertNull(emptyDto.getId());
        assertNull(emptyDto.getEmail());
        assertNull(emptyDto.getLastName());
        assertNull(emptyDto.getFirstName());
        assertFalse(emptyDto.isAdmin());
        assertNull(emptyDto.getPassword());
        assertNull(emptyDto.getCreatedAt());
        assertNull(emptyDto.getUpdatedAt());
    }

    @Test
    public void testGettersAndSetters() {
        UserDto dto = new UserDto();
        dto.setId(3L);
        dto.setEmail("alice@example.com");
        dto.setLastName("Smith");
        dto.setFirstName("Alice");
        dto.setAdmin(true);
        dto.setPassword("newSecurePass!");
        dto.setCreatedAt(LocalDateTime.of(2024, 3, 1, 10, 30));
        dto.setUpdatedAt(LocalDateTime.of(2024, 3, 5, 15, 45));

        assertEquals(3L, dto.getId());
        assertEquals("alice@example.com", dto.getEmail());
        assertEquals("Smith", dto.getLastName());
        assertEquals("Alice", dto.getFirstName());
        assertTrue(dto.isAdmin());
        assertEquals("newSecurePass!", dto.getPassword());
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
    public void testValidation_ValidUserDto_ShouldPass() {
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testValidation_InvalidEmail_ShouldFail() {
        userDto.setEmail("invalid-email");

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testValidation_LongEmail_ShouldFail() {
        userDto.setEmail("a".repeat(51) + "@example.com");

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testValidation_LongFirstName_ShouldFail() {
        userDto.setFirstName("a".repeat(21));

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testValidation_LongLastName_ShouldFail() {
        userDto.setLastName("a".repeat(21));

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testValidation_LongPassword_ShouldFail() {
        userDto.setPassword("a".repeat(121));

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertFalse(violations.isEmpty());
    }
}
