package com.professul.professul.domain.user.service;

import com.professul.professul.domain.user.entity.Status;
import com.professul.professul.domain.user.entity.User;
import com.professul.professul.domain.user.entity.UserRole;
import com.professul.professul.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ReportProcessingTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("사용자차단성공")
    void banUser() {
        //Given
        Long userId=1L;
        User user = new User(userId, "test@example.com", "Test User", "password", Status.ACTIVE, null, null, UserRole.ROLE_USER);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.banUser(userId);

        //Then
        assertEquals(Status.BANNED, user.getStatus());
        verify(userRepository,times(1)).save(user);
    }

    @Test
    @DisplayName("사용자 차단 실패 - 사용자를 찾을 수 없음")
    void banUserFailUserNotFound() {
        // Given
        Long userId = 2L;

        // Mock 설정
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UsernameNotFoundException.class, () -> userService.banUser(userId));
        verify(userRepository, times(0)).save(any(User.class)); // 사용자를 찾을 수 없기 때문에 save 메소드가 호출되지 않아야 함
    }



    @Test
    @DisplayName("사용자 탈퇴 성공")
    void withdrawUser(){
        //Given
        Long userId=1L;
        User user = new User(userId, "test@example.com", "Test User", "password", Status.ACTIVE, null, null, UserRole.ROLE_USER);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.withdrawUser(userId);

        //Then
        assertEquals(Status.CANCELED, user.getStatus());
        verify(userRepository,times(1)).save(user);
    }

    @Test
    @DisplayName("사용자 탈퇴 실패 - 사용자를 찾을 수 없음")
    void withdrawUserFailUserNotFound() {
        // Given
        Long userId = 2L;

        // Mock 설정
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UsernameNotFoundException.class, () -> userService.withdrawUser(userId));
        verify(userRepository, times(0)).save(any(User.class)); // 사용자를 찾을 수 없기 때문에 save 메소드가 호출되지 않아야 함
    }


}
