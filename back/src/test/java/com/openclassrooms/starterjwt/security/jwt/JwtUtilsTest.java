package com.openclassrooms.starterjwt.security.jwt;

import io.jsonwebtoken.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtUtilsTest {

    @InjectMocks
    private JwtUtils jwtUtils;

    @Mock
    private Jws<Claims> jwsClaims;

    private String validToken;
    private String expiredToken;
    private String invalidSignatureToken;
    private String malformedToken;

    private final int jwtExpirationMs = 1000;

    @BeforeEach
    public void setUp() {
        String secretKey = "testSecretKey";
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", secretKey);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", jwtExpirationMs);

        validToken = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();

        expiredToken = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date(System.currentTimeMillis() - 2000))
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();

        invalidSignatureToken = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, "wrongSecretKey")
                .compact();

        malformedToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.malformedPayload.signature";
    }

    @Test
    public void testValidateJwtToken_ValidToken() {
        assertTrue(jwtUtils.validateJwtToken(validToken));
    }

    @Test
    public void testValidateJwtToken_ExpiredToken() {
        assertFalse(jwtUtils.validateJwtToken(expiredToken));
    }

    @Test
    public void testValidateJwtToken_InvalidSignature() {
        assertFalse(jwtUtils.validateJwtToken(invalidSignatureToken));
    }

    @Test
    public void testValidateJwtToken_MalformedToken() {
        assertFalse(jwtUtils.validateJwtToken(malformedToken));
    }

    @Test
    public void testValidateJwtToken_EmptyToken() {
        String emptyToken = "";
        assertFalse(jwtUtils.validateJwtToken(emptyToken));
    }

    @Test
    public void testValidateJwtToken_UnsupportedToken() {
        String unsupportedToken = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .compact();

        assertFalse(jwtUtils.validateJwtToken(unsupportedToken));
    }

    @Test
    public void testGetUserNameFromJwtToken() {
        String username = jwtUtils.getUserNameFromJwtToken(validToken);
        assertEquals("testUser", username);
    }
}
