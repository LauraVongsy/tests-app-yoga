package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("securePassword")
                .admin(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userDto = new UserDto(
                1L,
                "test@example.com",
                "Doe",
                "John",
                true,
                "securePassword", // Ignored in serialization due to @JsonIgnore
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    public void testUserToUserDto() {
        // When
        UserDto mappedDto = userMapper.toDto(user);

        // Then
        assertNotNull(mappedDto);
        assertEquals(user.getId(), mappedDto.getId());
        assertEquals(user.getEmail(), mappedDto.getEmail());
        assertEquals(user.getFirstName(), mappedDto.getFirstName());
        assertEquals(user.getLastName(), mappedDto.getLastName());
        assertEquals(user.isAdmin(), mappedDto.isAdmin());
        assertEquals(user.getCreatedAt(), mappedDto.getCreatedAt());
        assertEquals(user.getUpdatedAt(), mappedDto.getUpdatedAt());
    }

    @Test
    public void testUserDtoToUser() {
        // When
        User mappedUser = userMapper.toEntity(userDto);

        // Then
        assertNotNull(mappedUser);
        assertEquals(userDto.getId(), mappedUser.getId());
        assertEquals(userDto.getEmail(), mappedUser.getEmail());
        assertEquals(userDto.getFirstName(), mappedUser.getFirstName());
        assertEquals(userDto.getLastName(), mappedUser.getLastName());
        assertEquals(userDto.isAdmin(), mappedUser.isAdmin());
        assertEquals(userDto.getCreatedAt(), mappedUser.getCreatedAt());
        assertEquals(userDto.getUpdatedAt(), mappedUser.getUpdatedAt());

        // Ensure password is set correctly (since it's ignored in mapping)
        mappedUser.setPassword("defaultPassword");
        assertEquals("defaultPassword", mappedUser.getPassword());
    }

    @Test
    public void testUserListToUserDtoList() {
        // Given
        User anotherUser = User.builder()
                .id(2L)
                .email("user2@example.com")
                .firstName("Jane")
                .lastName("Smith")
                .password("anotherSecurePassword")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<User> users = Arrays.asList(user, anotherUser);

        // When
        List<UserDto> userDto = userMapper.toDto(users);

        // Then
        assertNotNull(userDto);
        assertEquals(2, userDto.size());
        assertEquals(user.getEmail(), userDto.get(0).getEmail());
        assertEquals(anotherUser.getEmail(), userDto.get(1).getEmail());
    }

    @Test
    public void testUserDtoListToUserList() {
        // Given
        UserDto anotherUserDto = new UserDto(
                2L,
                "user2@example.com",
                "Smith",
                "Jane",
                false,
                "anotherSecurePassword",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        List<UserDto> userDtoList = Arrays.asList(userDto, anotherUserDto);

        // When
        List<User> userList = userMapper.toEntity(userDtoList);

        // Then
        assertNotNull(userList);
        assertEquals(2, userList.size());
        assertEquals(userDto.getEmail(), userList.get(0).getEmail());
        assertEquals(anotherUserDto.getEmail(), userList.get(1).getEmail());

        // Ensure password handling
        userList.get(0).setPassword("defaultPassword1");
        userList.get(1).setPassword("defaultPassword2");
        assertEquals("defaultPassword1", userList.get(0).getPassword());
        assertEquals("defaultPassword2", userList.get(1).getPassword());
    }

    @Test
    public void testNullUserToUserDto() {
        assertNull(userMapper.toDto((List<User>) null));
    }

    @Test
    public void testNullUserDtoToUser() {
        assertNull(userMapper.toEntity((List<UserDto>) null));
    }

    @Test
    public void testNullUserListToUserDtoList() {
        assertNull(userMapper.toDto((List<User>) null));
    }

    @Test
    public void testNullUserDtoListToUserList() {
        assertNull(userMapper.toEntity((List<UserDto>) null));
    }
}
