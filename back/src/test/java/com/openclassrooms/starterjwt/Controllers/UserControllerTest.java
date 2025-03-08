package com.openclassrooms.starterjwt.Controllers;
import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testFindById_UserExists() throws Exception {
        // Given
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@example.com");

        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        // When & Then
        mockMvc.perform(get("/api/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(userService, times(1)).findById(1L);
    }


    @Test
    public void testFindById_UserNotFound() throws Exception {
        // Given
        when(userService.findById(1L)).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/api/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).findById(1L);
    }

    @Test
    public void testFindById_InvalidId() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/user/abc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDelete_UserExists_AndAuthorized() throws Exception {
        // Given
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        when(userService.findById(1L)).thenReturn(user);
        when(userDetails.getUsername()).thenReturn("test@example.com");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // When & Then
        mockMvc.perform(delete("/api/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).delete(1L);
    }

    @Test
    public void testDelete_UserExists_ButUnauthorized() throws Exception {
        // Given
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        when(userService.findById(1L)).thenReturn(user);
        when(userDetails.getUsername()).thenReturn("another@example.com");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // When & Then
        mockMvc.perform(delete("/api/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        verify(userService, never()).delete(1L);
    }

    @Test
    public void testDelete_UserNotFound() throws Exception {
        // Given
        when(userService.findById(1L)).thenReturn(null);

        // When & Then
        mockMvc.perform(delete("/api/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(userService, never()).delete(1L);
    }

    @Test
    public void testDelete_InvalidId() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/user/abc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
