package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionMapperTest {

    @InjectMocks
    private SessionMapperImpl sessionMapper;

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    private Session session;
    private SessionDto sessionDto;
    private Teacher teacher;
    private User user1, user2;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        user1 = new User();
        user1.setId(100L);
        user1.setFirstName("Alice");
        user1.setLastName("Brown");

        user2 = new User();
        user2.setId(101L);
        user2.setFirstName("Bob");
        user2.setLastName("Smith");

        session = Session.builder()
                .id(1L)
                .name("Math Session")
                .date(new Date())
                .description("Advanced Math Course")
                .teacher(teacher)
                .users(Arrays.asList(user1, user2))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        sessionDto = new SessionDto(
                1L,
                "Math Session",
                new Date(),
                1L,
                "Advanced Math Course",
                Arrays.asList(100L, 101L),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    public void testSessionToSessionDto() {
        // When
        SessionDto mappedDto = sessionMapper.toDto(session);

        // Then
        assertNotNull(mappedDto);
        assertEquals(session.getId(), mappedDto.getId());
        assertEquals(session.getName(), mappedDto.getName());
        assertEquals(session.getDate(), mappedDto.getDate());
        assertEquals(session.getDescription(), mappedDto.getDescription());
        assertEquals(session.getTeacher().getId(), mappedDto.getTeacher_id());
        assertEquals(session.getUsers().size(), mappedDto.getUsers().size());
        assertTrue(mappedDto.getUsers().contains(user1.getId()));
        assertTrue(mappedDto.getUsers().contains(user2.getId()));
    }

    @Test
    public void testSessionDtoToSession() {
        // Mock TeacherService and UserService
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(100L)).thenReturn(user1);
        when(userService.findById(101L)).thenReturn(user2);

        // When
        Session mappedSession = sessionMapper.toEntity(sessionDto);

        // Then
        assertNotNull(mappedSession);
        assertEquals(sessionDto.getId(), mappedSession.getId());
        assertEquals(sessionDto.getName(), mappedSession.getName());
        assertEquals(sessionDto.getDate(), mappedSession.getDate());
        assertEquals(sessionDto.getDescription(), mappedSession.getDescription());
        assertEquals(sessionDto.getTeacher_id(), mappedSession.getTeacher().getId());
        assertEquals(sessionDto.getUsers().size(), mappedSession.getUsers().size());
        assertTrue(mappedSession.getUsers().stream().anyMatch(user -> user.getId().equals(100L)));
        assertTrue(mappedSession.getUsers().stream().anyMatch(user -> user.getId().equals(101L)));

        // Verify interactions with mocked services
        verify(teacherService, times(1)).findById(1L);
        verify(userService, times(1)).findById(100L);
        verify(userService, times(1)).findById(101L);
    }

    @Test
    public void testSessionListToSessionDtoList() {
        // Given
        List<Session> sessions = Collections.singletonList(session);

        // When
        List<SessionDto> sessionDtos = sessionMapper.toDto(sessions);

        // Then
        assertNotNull(sessionDtos);
        assertEquals(1, sessionDtos.size());
        assertEquals(session.getId(), sessionDtos.get(0).getId());
    }

    @Test
    public void testSessionDtoListToSessionList() {
        // Mocking services
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(100L)).thenReturn(user1);
        when(userService.findById(101L)).thenReturn(user2);

        // Given
        List<SessionDto> sessionDtoList = Collections.singletonList(sessionDto);

        // When
        List<Session> sessionList = sessionMapper.toEntity(sessionDtoList);

        // Then
        assertNotNull(sessionList);
        assertEquals(1, sessionList.size());
        assertEquals(sessionDto.getId(), sessionList.get(0).getId());

        // Verify service calls
        verify(teacherService, times(1)).findById(1L);
        verify(userService, times(1)).findById(100L);
        verify(userService, times(1)).findById(101L);
    }

    @Test
    public void testNullSessionToSessionDto() {
        assertNull(sessionMapper.toDto((List<Session>) null));
    }

    @Test
    public void testNullSessionDtoToSession() {
        assertNull(sessionMapper.toEntity((List<SessionDto>) null));
    }

    @Test
    public void testNullSessionListToSessionDtoList() {
        assertNull(sessionMapper.toDto((List<Session>) null));
    }

    @Test
    public void testNullSessionDtoListToSessionList() {
        assertNull(sessionMapper.toEntity((List<SessionDto>) null));
    }
}
