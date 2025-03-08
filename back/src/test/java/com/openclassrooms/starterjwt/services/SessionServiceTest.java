package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    private Session session;
    private User user;

    @BeforeEach
    void setUp() {
        session = new Session();
        session.setId(1L);
        session.setUsers(new ArrayList<>());

        user = new User();
        user.setId(2L);
    }

    @Test
    public void testCreate() {
        when(sessionRepository.save(session)).thenReturn(session);

        Session createdSession = sessionService.create(session);

        assertNotNull(createdSession);
        assertEquals(session, createdSession);
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    public void testDelete() {
        Long sessionId = 1L;
        doNothing().when(sessionRepository).deleteById(sessionId);

        sessionService.delete(sessionId);

        verify(sessionRepository, times(1)).deleteById(sessionId);
    }

    @Test
    public void testFindAll() {
        List<Session> sessions = List.of(new Session(), new Session());
        when(sessionRepository.findAll()).thenReturn(sessions);

        List<Session> result = sessionService.findAll();

        assertEquals(2, result.size());
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    public void testGetById() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        Session result = sessionService.getById(1L);

        assertNotNull(result);
        assertEquals(session, result);
        verify(sessionRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetById_NotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        Session result = sessionService.getById(1L);

        assertNull(result);
        verify(sessionRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdate() {
        when(sessionRepository.save(session)).thenReturn(session);

        Session updatedSession = sessionService.update(1L, session);

        assertNotNull(updatedSession);
        assertEquals(1L, updatedSession.getId());
        verify(sessionRepository, times(1)).save(session);
    }

    // Tests for participate feature
    @Test
    public void testParticipate_Success() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        assertDoesNotThrow(() -> sessionService.participate(1L, 2L));

        assertTrue(session.getUsers().contains(user));
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    public void testParticipate_SessionNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 2L));

        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    public void testParticipate_UserNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 2L));

        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    public void testParticipate_AlreadyParticipating() {
        session.getUsers().add(user);

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> sessionService.participate(1L, 2L));

        verify(sessionRepository, never()).save(any(Session.class));
    }

    // Tests for noLongerParticipate feature
    @Test
    public void testNoLongerParticipate_Success() {
        session.getUsers().add(user);

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        assertDoesNotThrow(() -> sessionService.noLongerParticipate(1L, 2L));

        assertFalse(session.getUsers().contains(user));
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    public void testNoLongerParticipate_SessionNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(1L, 2L));

        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    public void testNoLongerParticipate_UserNotParticipating() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(1L, 2L));

        verify(sessionRepository, never()).save(any(Session.class));
    }
}
