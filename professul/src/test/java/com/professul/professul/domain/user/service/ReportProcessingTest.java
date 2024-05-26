package com.professul.professul.domain.user.service;

import com.professul.professul.domain.user.entity.Status;
import com.professul.professul.domain.user.entity.User;
import com.professul.professul.domain.user.entity.UserRole;
import com.professul.professul.domain.user.repository.UserRepository;
import com.professul.professul.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
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
    @DisplayName("사용자정지성공")
    void suspendUser() throws Exception {
        //Given
        Long userId=1L;
        LocalDate suspendUntil=LocalDate.now().plusDays(7);
        User user = new User(userId, "test@example.com", "Test User", "password", Status.ACTIVE, UserRole.ROLE_USER);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        //when
        userService.suspendUser(userId,suspendUntil);

        //Then
        assertEquals(Status.SUSPENDED, user.getStatus());
        assertEquals(suspendUntil, user.getSuspendUntil()); // 일시 정지 기간도 확인
        verify(userRepository,times(1)).save(user);

    }


    @Test
    @DisplayName("사용자 정지 실패 - 사용자를 찾을 수 없음")
    void suspendUserFailUserNotFound() {
        // Given
        Long userId = 2L;
        LocalDate suspendUntil=LocalDate.now().plusDays(7);

        // Mock 설정
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UsernameNotFoundException.class, () -> userService.suspendUser(userId,suspendUntil));
        verify(userRepository, times(0)).save(any(User.class)); // 사용자를 찾을 수 없기 때문에 save 메소드가 호출되지 않아야 함
    }


    @Test
    @DisplayName("사용자차단성공")
    void banUser() {
        //Given
        Long userId=1L;
        String reason= "욕설누적";
        User user = new User(userId, "test@example.com", "Test User", "password", Status.ACTIVE, UserRole.ROLE_USER);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.banUser(userId, reason);

        //Then
        assertEquals(Status.BANNED, user.getStatus());
        verify(userRepository,times(1)).save(user);
    }

    @Test
    @DisplayName("사용자 차단 실패 - 사용자를 찾을 수 없음")
    void banUserFailUserNotFound() {
        // Given
        Long userId = 2L;
        String reason= "욕설누적";
        // Mock 설정
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UsernameNotFoundException.class, () -> userService.banUser(userId,reason));
        verify(userRepository, times(0)).save(any(User.class)); // 사용자를 찾을 수 없기 때문에 save 메소드가 호출되지 않아야 함
    }



    @Test
    @DisplayName("사용자 탈퇴 성공")
    void withdrawUser(){
        //Given
        Long userId=1L;
        User user = new User(userId, "test@example.com", "Test User", "password", Status.ACTIVE, UserRole.ROLE_USER);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.withdrawUser(userId);

        //Then
        assertEquals(Status.CANCELED, user.getStatus());
        verify(userRepository,times(1)).save(user);
    }

    @Test
    @DisplayName("사용자 활성화 성공")
    void activateUser() throws Exception {
        // Given
        Long userId = 1L;
        User inactiveUser = new User(userId, "test@example.com", "Test User", "password", Status.SUSPENDED, UserRole.ROLE_USER);

        when(userRepository.findById(userId)).thenReturn(Optional.of(inactiveUser));

        // When
        userService.activateUser(userId);

        // Then
        assertEquals(Status.ACTIVE, inactiveUser.getStatus());
        verify(userRepository, times(1)).save(inactiveUser);
    }

    @Test
    @DisplayName("사용자 활성화 실패 - 사용자 없음")
    void activateUser_UserNotFound() {
        // Given
        Long userId = 2L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UsernameNotFoundException.class, () -> userService.activateUser(userId));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("사용자 활성화 실패 - 이미 활성화된 사용자")
    void activateUser_UserAlreadyActive() {
        // Given
        Long userId = 1L;
        User activeUser = new User(userId, "test@example.com", "Test User", "password", Status.ACTIVE, UserRole.ROLE_USER);

        when(userRepository.findById(userId)).thenReturn(Optional.of(activeUser));

        // When & Then
        assertThrows(IllegalStateException.class, () -> userService.activateUser(userId));
        verify(userRepository, never()).save(any(User.class));
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
