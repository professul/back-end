package com.professul.professul.domain.user.service;

import com.professul.professul.domain.user.entity.Status;
import com.professul.professul.domain.user.entity.User;
import com.professul.professul.domain.user.entity.UserRole;
import com.professul.professul.domain.user.repository.UserRepository;
import com.professul.professul.exception.UserModificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ChangeUserInfoTest {


    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("사용자이름변경성공")
    void modifyUserName() throws UserModificationException {
        // Given
        Long userId = 1L;
        String newName = "New Name";

        User user = new User(userId, "test@example.com", "Old Name", "password", Status.ACTIVE, UserRole.ROLE_USER);

        when(userRepository.findByUserId(userId)).thenReturn(user);

        // When
        userService.modifyUserName(userId, newName);

        // Then
        assertEquals(newName, user.getName()); //사용자 객체의 이름이 변경되었는지 확인
        verify(userRepository, times(1)).save(user);

    }

    @Test
    @DisplayName("사용자찾을수없을때")
    void modifyUserNameUserNotFound() {
        // Given
        Long userId = 2L;
        String newName = "New Name";

        when(userRepository.findByUserId(userId)).thenReturn(null);

        // When & Then
        assertThrows(UserModificationException.class, () -> userService.modifyUserName(userId, newName));
    }
}

