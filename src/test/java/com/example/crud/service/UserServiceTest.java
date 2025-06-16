package com.example.crud.service;

import com.example.crud.model.User;
import com.example.crud.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRoles(Set.of("USER"));
        user.setEnabled(true);
    }

    @Test
    void getAllUsers() {
        // Arrange
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("testuser2");
        when(userRepository.findAll()).thenReturn(Arrays.asList(user, user2));

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        assertEquals(user, userService.getUserById(1L).orElse(null));
        assertNull(userService.getUserById(2L).orElse(null));
    }

    @Test
    void getUserByUsername() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        assertEquals(user, userService.getUserByUsername("testuser").orElse(null));
        assertNull(userService.getUserByUsername("nonexistent").orElse(null));
    }

    @Test
    void createUser() {
        // Arrange
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User newUser = new User("newuser", "password");
        User savedUser = userService.createUser(newUser);

        // Assert
        assertEquals(user, savedUser);
        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser() {
        // Arrange
        User updatedDetails = new User();
        updatedDetails.setUsername("updateduser");
        updatedDetails.setPassword("newpassword");
        updatedDetails.setRoles(Set.of("USER", "ADMIN"));
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newpassword")).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenReturn(updatedDetails);

        // Act
        User result = userService.updateUser(1L, updatedDetails);

        // Assert
        assertEquals("updateduser", result.getUsername());
        assertEquals(2, result.getRoles().size());
        assertTrue(result.getRoles().contains("ADMIN"));
        verify(passwordEncoder, times(1)).encode("newpassword");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUserNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> 
            userService.updateUser(1L, new User())
        );
    }

    @Test
    void deleteUser() {
        // Arrange
        when(userRepository.existsById(1L)).thenReturn(true);

        // Act
        userService.deleteUser(1L);

        // Assert
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUserNotFound() {
        // Arrange
        when(userRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> 
            userService.deleteUser(1L)
        );
    }

    @Test
    void usernameExists() {
        // Arrange
        when(userRepository.existsByUsername("testuser")).thenReturn(true);
        when(userRepository.existsByUsername("nonexistent")).thenReturn(false);

        // Act & Assert
        assertTrue(userService.usernameExists("testuser"));
        assertFalse(userService.usernameExists("nonexistent"));
    }
}
