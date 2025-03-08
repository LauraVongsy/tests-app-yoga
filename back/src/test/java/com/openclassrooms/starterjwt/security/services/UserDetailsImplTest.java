package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserDetailsImplTest {

    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("testUser")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("securePassword")
                .build();
    }

    @Test
    public void testIsAccountNonExpired_ShouldReturnTrue() {
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    public void testIsAccountNonLocked_ShouldReturnTrue() {
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    public void testIsCredentialsNonExpired_ShouldReturnTrue() {
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    public void testIsEnabled_ShouldReturnTrue() {
        assertTrue(userDetails.isEnabled());
    }

    @Test
    public void testEquals_SameInstance_ShouldReturnTrue() {
        assertEquals(userDetails, userDetails);
    }

    @Test
    public void testEquals_SameId_ShouldReturnTrue() {
        UserDetailsImpl anotherUser = UserDetailsImpl.builder()
                .id(1L)
                .username("differentUser")
                .firstName("Jane")
                .lastName("Smith")
                .admin(true)
                .password("anotherPassword")
                .build();

        assertEquals(userDetails, anotherUser);
    }

    @Test
    public void testEquals_DifferentId_ShouldReturnFalse() {
        UserDetailsImpl anotherUser = UserDetailsImpl.builder()
                .id(2L)
                .username("differentUser")
                .firstName("Jane")
                .lastName("Smith")
                .admin(true)
                .password("anotherPassword")
                .build();

        assertNotEquals(userDetails, anotherUser);
    }

    @Test
    public void testEquals_NullObject_ShouldReturnFalse() {
        assertNotEquals(null, userDetails);
    }

    @Test
    public void testEquals_DifferentClass_ShouldReturnFalse() {
        Object otherObject = new Object();
        assertNotEquals(userDetails, otherObject);
    }
}
