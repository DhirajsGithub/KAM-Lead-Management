package com.udaan.kam.kam_lead_management;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.udaan.kam.kam_lead_management.DTO.UserDetailDTO;
import com.udaan.kam.kam_lead_management.entity.User;
import com.udaan.kam.kam_lead_management.entity.User.Role;
import com.udaan.kam.kam_lead_management.exception.UserNotFoundException;
import com.udaan.kam.kam_lead_management.repository.UserRepository;
import com.udaan.kam.kam_lead_management.service.UserService;
import com.udaan.kam.kam_lead_management.util.DTOConverterUtil;

@ExtendWith(MockitoExtension.class)
public class UserTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DTOConverterUtil dtoConverter;

    @InjectMocks
    private UserService userService;

    private User sampleUser;
    private UserDetailDTO sampleUserDetailDTO;

    @BeforeEach
    void setUp() {
        sampleUser = new User(
            "testuser", "testuser@example.com", "password123",
            "Test", "User", Role.MANAGER
        );
        sampleUser.setId(1);
        sampleUser.setCreatedAt(LocalDateTime.now());

        sampleUserDetailDTO = new UserDetailDTO(
            1, "testuser", "testuser@example.com", "Test", "User",
            "USER", sampleUser.getCreatedAt(), true, null, null
        );
    }

    // Unit Tests

    @Test
    void testCreateUser_Success() {
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);
        when(dtoConverter.convertToUserDetailDto(any(User.class))).thenReturn(sampleUserDetailDTO);

        UserDetailDTO result = userService.createUser(sampleUser);

        assertNotNull(result);
        assertEquals(sampleUser.getUsername(), result.getUsername());
        verify(userRepository, times(1)).save(sampleUser);
    }

    @Test
    void testCreateUser_MissingUsername() {
        sampleUser.setUsername(null);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(sampleUser);
        });

        assertTrue(exception.getMessage().contains("Username is required."));
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void testGetUserById_Success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(sampleUser));
        when(dtoConverter.convertToUserDetailDto(any(User.class))).thenReturn(sampleUserDetailDTO);

        UserDetailDTO result = userService.getUserDetailsById(1);

        assertNotNull(result);
        assertEquals(sampleUser.getUsername(), result.getUsername());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getUserDetailsById(1);
        });

        assertTrue(exception.getMessage().contains("User not found with ID"));
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testUpdateUser_Success() {
        User updatedUser = new User(
            "updatedUser", "updated@example.com", "newPassword",
            "Updated", "User", Role.ADMIN
        );

        when(userRepository.findById(1)).thenReturn(Optional.of(sampleUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.updateUser(1, updatedUser);

        assertNotNull(result);
        assertEquals(updatedUser.getUsername(), result.getUsername());
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_NotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.updateUser(1, sampleUser);
        });

        assertTrue(exception.getMessage().contains("User not found with ID"));
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.existsById(1)).thenReturn(true);

        userService.deleteUser(1);

        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.existsById(1)).thenReturn(false);

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUser(1);
        });

        assertTrue(exception.getMessage().contains("User not found with ID"));
        verify(userRepository, times(0)).deleteById(anyInt());
    }

    // Edge Case Testing

    @Test
    void testFindByUsername_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(sampleUser));
        when(dtoConverter.convertToUserDetailDto(any(User.class))).thenReturn(sampleUserDetailDTO);

        UserDetailDTO result = userService.findByUsername("testuser");

        assertNotNull(result);
        assertEquals(sampleUser.getUsername(), result.getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testFindByUsername_NotFound() {
        when(userRepository.findByUsername("nonexistentuser")).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.findByUsername("nonexistentuser");
        });

        assertTrue(exception.getMessage().contains("Current User not found"));
        verify(userRepository, times(1)).findByUsername("nonexistentuser");
    }

    // Integration Test

    @DataJpaTest
    static class UserServiceIntegrationTest {

        @Autowired
        private UserRepository userRepository;

        @Test
        void testCreateAndRetrieveUser() {
            User user = new User(
                "integrationTest", "integration@example.com", "securePassword",
                "Integration", "Test", Role.MANAGER
            );

            User savedUser = userRepository.save(user);

            Optional<User> foundUser = userRepository.findById(savedUser.getId());

            assertTrue(foundUser.isPresent());
            assertEquals("integrationTest", foundUser.get().getUsername());
        }
    }
}
