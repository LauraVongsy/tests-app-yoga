package com.openclassrooms.starterjwt.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.openclassrooms.starterjwt.SpringBootSecurityJwtApplication;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SpringBootSecurityJwtApplication.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void authenticateAdminUser() throws Exception {
        LoginRequest loginRequest = createValidLoginRequest("yoga@studio.com", "test!1234");
        String requestBody = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.username", is("yoga@studio.com")))
                .andExpect(jsonPath("$.admin", is(true)));
    }

    @Test
    public void authenticateUserWithInvalidCredentials() throws Exception {
        LoginRequest loginRequest = createValidLoginRequest("invaliduser@gmail.com", "wrongPassword");
        String requestBody = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void authenticateUserWithEmptyCredentials() throws Exception {
        LoginRequest loginRequest = createValidLoginRequest("", "");
        String requestBody = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void registerUser() throws Exception {
        SignupRequest signupRequest = createNonAdminUser();
        String requestBody = objectMapper.writeValueAsString(signupRequest);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("User registered successfully!")));
    }

    @Test
    public void registerExistingUser() throws Exception {
        SignupRequest signupRequest = createNonAdminUser();
        String requestBody = objectMapper.writeValueAsString(signupRequest);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Error: Email is already taken!")));
    }

    @Test
    public void registerUserWithInvalidEmail() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("invalid-email");
        signupRequest.setPassword("validPass!123");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");

        String requestBody = objectMapper.writeValueAsString(signupRequest);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void registerUserWithWeakPassword() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("weakpassword@gmail.com");
        signupRequest.setPassword("123");
        signupRequest.setFirstName("Weak");
        signupRequest.setLastName("User");

        String requestBody = objectMapper.writeValueAsString(signupRequest);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void registerUserWithMissingFields() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        String requestBody = objectMapper.writeValueAsString(signupRequest);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @AfterAll
    public void cleanup() throws Exception {
        SignupRequest signupRequest = createNonAdminUser();
        String requestBody = objectMapper.writeValueAsString(signupRequest);

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String token = JsonPath.read(content, "$.token");
        int id = JsonPath.read(content, "$.id");

        mockMvc.perform(delete("/api/user/" + id).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    private SignupRequest createNonAdminUser() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("newuser@gmail.com");
        signupRequest.setPassword("SecurePass!123");
        signupRequest.setFirstName("New");
        signupRequest.setLastName("User");
        return signupRequest;
    }

    private LoginRequest createValidLoginRequest(String email, String password) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);
        return loginRequest;
    }
}
