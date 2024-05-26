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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

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

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

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

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UsernameNotFoundException.class, () -> userService.modifyUserName(userId, newName));
    }

    @Test
    @DisplayName("오류났을때")
    void modifyUserNameError(){
        //Given
        Long userId= 1L;
        String newName= "New Name";

        User user = new User(userId, "test@example.com", "Old Name", "password", Status.ACTIVE, UserRole.ROLE_USER);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("데이터베이스 오류"));
        // When & Then
        assertThrows(UserModificationException.class, () -> {
            userService.modifyUserName(userId, newName);
        });

        // userRepository.findById가 호출되었는지 확인
        verify(userRepository).findById(userId);
        // userRepository.save가 호출되었는지 확인
        verify(userRepository).save(any(User.class));
    }


    }

