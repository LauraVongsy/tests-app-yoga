package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SignupRequestTest {

    private Validator validator;
    private SignupRequest signupRequest;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("securePassword123");
    }

    @Test
    public void testValidSignupRequest_ShouldPassValidation() {
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidEmail_ShouldFailValidation() {
        signupRequest.setEmail("invalid-email");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBlankEmail_ShouldFailValidation() {
        signupRequest.setEmail("");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBlankFirstName_ShouldFailValidation() {
        signupRequest.setFirstName("");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testShortFirstName_ShouldFailValidation() {
        signupRequest.setFirstName("Jo"); // Less than 3 characters

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testLongFirstName_ShouldFailValidation() {
        signupRequest.setFirstName("ThisIsAVeryLongFirstNameExceedingLimit");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBlankLastName_ShouldFailValidation() {
        signupRequest.setLastName("");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testShortLastName_ShouldFailValidation() {
        signupRequest.setLastName("Li"); // Less than 3 characters

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testLongLastName_ShouldFailValidation() {
        signupRequest.setLastName("ThisIsAVeryLongLastNameExceedingLimit");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testBlankPassword_ShouldFailValidation() {
        signupRequest.setPassword("");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testShortPassword_ShouldFailValidation() {
        signupRequest.setPassword("12345"); // Less than 6 characters

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testLongPassword_ShouldFailValidation() {
        signupRequest.setPassword("ThisIsAReallyLongPasswordThatExceedsFortyCharacters123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testSettersAndGetters() {
        signupRequest.setEmail("new@example.com");
        signupRequest.setFirstName("Alice");
        signupRequest.setLastName("Brown");
        signupRequest.setPassword("NewSecurePassword123");

        assertEquals("new@example.com", signupRequest.getEmail());
        assertEquals("Alice", signupRequest.getFirstName());
        assertEquals("Brown", signupRequest.getLastName());
        assertEquals("NewSecurePassword123", signupRequest.getPassword());
    }
}

