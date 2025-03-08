package com.openclassrooms.starterjwt.Controllers;

import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class SessionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private SessionController sessionController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(sessionController).build();
    }

    @Test
    public void testFindById_SessionExists() throws Exception {
        // Given
        Session session = new Session();
        session.setId(1L);

        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);

        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // When & Then
        mockMvc.perform(get("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(sessionService, times(1)).getById(1L);
    }

    @Test
    public void testFindById_SessionNotFound() throws Exception {
        // Given
        when(sessionService.getById(1L)).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(sessionService, times(1)).getById(1L);
    }

    @Test
    public void testFindById_InvalidId() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/session/abc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testFindAll_SessionsExist() throws Exception {
        // Given
        Session session1 = new Session();
        session1.setId(1L);

        Session session2 = new Session();
        session2.setId(2L);

        List<Session> sessions = Arrays.asList(session1, session2);

        SessionDto sessionDto1 = new SessionDto();
        sessionDto1.setId(1L);

        SessionDto sessionDto2 = new SessionDto();
        sessionDto2.setId(2L);

        List<SessionDto> sessionDto = Arrays.asList(sessionDto1, sessionDto2);

        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(sessionDto);

        // When & Then
        mockMvc.perform(get("/api/session")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(sessionService, times(1)).findAll();
    }

    @Test
    public void testCreate_Session() throws Exception {
        // Given
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);

        Session session = new Session();
        session.setId(1L);

        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(session);
        when(sessionService.create(any(Session.class))).thenReturn(session);
        when(sessionMapper.toDto(any(Session.class))).thenReturn(sessionDto);

        // When & Then
        mockMvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "            \"name\": \"Session Updated\",\n" +
                                "            \"date\": \"2025-03-05T14:00:00\",\n" +
                                "            \"teacher_id\": 2,\n" +
                                "            \"description\": \"Nouvelle description\"\n" +
                                "        }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(sessionService, times(1)).create(any(Session.class));
    }

    @Test
    public void testUpdate_Session() throws Exception {
        // Given
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);

        Session session = new Session();
        session.setId(1L);

        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(session);
        when(sessionService.update(eq(1L), any(Session.class))).thenReturn(session);
        when(sessionMapper.toDto(any(Session.class))).thenReturn(sessionDto);

        // When & Then
        mockMvc.perform(put("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"name\": \"Session Updated\", \"date\": \"2025-03-05T14:00:00\", \"teacher_id\": 2, \"description\": \"Nouvelle description\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(sessionService, times(1)).update(eq(1L), any(Session.class));
    }

    @Test
    public void testDelete_SessionExists() throws Exception {
        // Given
        Session session = new Session();
        session.setId(1L);

        when(sessionService.getById(1L)).thenReturn(session);
        doNothing().when(sessionService).delete(1L);

        // When & Then
        mockMvc.perform(delete("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(sessionService, times(1)).delete(1L);
    }

    @Test
    public void testDelete_SessionNotFound() throws Exception {
        // Given
        when(sessionService.getById(1L)).thenReturn(null);

        // When & Then
        mockMvc.perform(delete("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(sessionService, never()).delete(1L);
    }

    @Test
    public void testParticipate_Success() throws Exception {
        // Given
        doNothing().when(sessionService).participate(1L, 2L);

        // When & Then
        mockMvc.perform(post("/api/session/1/participate/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(sessionService, times(1)).participate(1L, 2L);
    }

    @Test
    public void testParticipate_InvalidId() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/session/abc/participate/xyz")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testNoLongerParticipate_Success() throws Exception {
        // Given
        doNothing().when(sessionService).noLongerParticipate(1L, 2L);

        // When & Then
        mockMvc.perform(delete("/api/session/1/participate/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(sessionService, times(1)).noLongerParticipate(1L, 2L);
    }



    @Test
    public void testUpdate_InvalidId() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);

        mockMvc.perform(put("/api/session/invalidId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"name\": \"Updated Session\", \"date\": \"2025-03-05T14:00:00\", \"teacher_id\": 2, \"description\": \"Updated description\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDelete_InvalidId() throws Exception {
        mockMvc.perform(delete("/api/session/invalidId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testNoLongerParticipate_InvalidId() throws Exception {
        mockMvc.perform(delete("/api/session/invalidId/participate/invalidUserId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}

