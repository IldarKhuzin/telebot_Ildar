package ru.ildar.surveybot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.ildar.surveybot.entity.UserEntity;
import ru.ildar.surveybot.repository.UserRepository;
import ru.ildar.surveybot.service.impl.UserServiceImpl;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        UserEntity user = new UserEntity();
        user.setName("Test");
        user.setEmail("test@mail.com");
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);
        UserEntity created = userService.createUser("Test", "test@mail.com");
        assertEquals("Test", created.getName());
        assertEquals("test@mail.com", created.getEmail());
    }

    @Test
    void testGetUserByEmail() {
        UserEntity user = new UserEntity();
        user.setEmail("test@mail.com");
        when(userRepository.findByEmail("test@mail.com")).thenReturn(user);
        UserEntity found = userService.getUserByEmail("test@mail.com");
        assertNotNull(found);
        assertEquals("test@mail.com", found.getEmail());
    }

    @Test
    void testGetAllUsers() {
        UserEntity user1 = new UserEntity();
        UserEntity user2 = new UserEntity();
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        List<UserEntity> users = userService.getAllUsers();
        assertEquals(2, users.size());
    }
} 