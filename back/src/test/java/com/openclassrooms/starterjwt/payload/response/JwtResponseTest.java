package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtResponseTest {

    @Test
    public void testConstructorAndGetters() {
        String token = "test-jwt-token";
        Long id = 1L;
        String username = "testUser";
        String firstName = "John";
        String lastName = "Doe";
        Boolean admin = true;

        JwtResponse jwtResponse = new JwtResponse(token, id, username, firstName, lastName, admin);

        assertEquals(token, jwtResponse.getToken());
        assertEquals("Bearer", jwtResponse.getType()); // Default value should be "Bearer"
        assertEquals(id, jwtResponse.getId());
        assertEquals(username, jwtResponse.getUsername());
        assertEquals(firstName, jwtResponse.getFirstName());
        assertEquals(lastName, jwtResponse.getLastName());
        assertEquals(admin, jwtResponse.getAdmin());
    }

    @Test
    public void testSetters() {
        JwtResponse jwtResponse = new JwtResponse("initial-token", 2L, "initialUser", "Initial", "User", false);

        jwtResponse.setToken("new-token");
        jwtResponse.setId(3L);
        jwtResponse.setUsername("newUser");
        jwtResponse.setFirstName("Jane");
        jwtResponse.setLastName("Smith");
        jwtResponse.setAdmin(true);

        assertEquals("new-token", jwtResponse.getToken());
        assertEquals(3L, jwtResponse.getId());
        assertEquals("newUser", jwtResponse.getUsername());
        assertEquals("Jane", jwtResponse.getFirstName());
        assertEquals("Smith", jwtResponse.getLastName());
        assertTrue(jwtResponse.getAdmin());
    }
}
