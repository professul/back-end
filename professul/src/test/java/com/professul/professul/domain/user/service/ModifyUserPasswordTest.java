package com.professul.professul.domain.user.service;

import com.professul.professul.domain.user.dto.ChangePasswordDto;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ModifyUserPasswordTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("사용자비밀번호변경성공")
    void modifyUserPassword() {
        // Given
        Long userId = 1L;
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        String confirmPassword="newPassword";
        ChangePasswordDto changePasswordDto = new ChangePasswordDto(currentPassword, newPassword, confirmPassword);
        User user = new User(userId, "test@example.com", "Test User", currentPassword, Status.ACTIVE, null, null, UserRole.ROLE_USER);

        // Mock 설정
        when(userRepository.findByUserId(userId)).thenReturn(user);
        when(bCryptPasswordEncoder.matches(currentPassword, user.getPassword())).thenReturn(true);
        when(bCryptPasswordEncoder.matches(newPassword, user.getPassword())).thenReturn(false);
        when(bCryptPasswordEncoder.encode(newPassword)).thenReturn("newEncryptedPassword");

        // When
        userService.modifyUserPassword(userId, changePasswordDto);

        // Then
        assertEquals("newEncryptedPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("사용자를 찾을 수 없는 경우")
    void modifyUserPassword_userNotFound(){
        //Given
        Long userId= 1L;
        ChangePasswordDto changePasswordDto = new ChangePasswordDto("currentPassword", "newPassword", "confirmPassword");

        when(userRepository.findByUserId(userId)).thenReturn(null);
        // When & Then
        assertThrows(UserModificationException.class, () -> userService.modifyUserPassword(userId, changePasswordDto));

    }

    @Test
    @DisplayName("현재 비밀번호가 일치하지 않는 경우")
    void modifyUserPassword_currentPasswordMismatch() {
        // Given
        Long userId = 1L;
        ChangePasswordDto changePasswordDto = new ChangePasswordDto("wrongCurrentPassword", "newPassword", "confirmPassword");
        User user = new User(userId, "test@example.com", "Test User", "encryptedPassword", Status.ACTIVE, null, null, UserRole.ROLE_USER);

        when(userRepository.findByUserId(userId)).thenReturn(user);
        when(bCryptPasswordEncoder.matches("wrongCurrentPassword", user.getPassword())).thenReturn(false);

        // When & Then
        assertThrows(UserModificationException.class, () -> userService.modifyUserPassword(userId, changePasswordDto));

    }

    @Test
    @DisplayName("새 비밀번호가 기존 비밀번호와 같은 경우")
    void modifyUserPassword_newPasswordSameAsOld() {
        // Given
        Long userId = 1L;
        ChangePasswordDto changePasswordDto = new ChangePasswordDto("currentPassword", "samePassword", "confirmPassword");
        User user = new User(userId, "test@example.com", "Test User", "encryptedPassword", Status.ACTIVE, null, null, UserRole.ROLE_USER);

        when(userRepository.findByUserId(userId)).thenReturn(user);
        when(bCryptPasswordEncoder.matches("currentPassword", user.getPassword())).thenReturn(true);
        when(bCryptPasswordEncoder.matches("samePassword", user.getPassword())).thenReturn(true);

        // When & Then
        assertThrows(UserModificationException.class, () -> userService.modifyUserPassword(userId, changePasswordDto));
    }

    @Test
    @DisplayName("새 비밀번호와 확인 비밀번호가 일치하지 않는 경우")
    void modifyUserPassword_newPasswordMismatch() {
        // Given
        Long userId = 1L;
        ChangePasswordDto changePasswordDto = new ChangePasswordDto("currentPassword", "newPassword", "differentConfirmPassword");
        User user = new User(userId, "test@example.com", "Test User", "encryptedPassword", Status.ACTIVE, null, null, UserRole.ROLE_USER);

        when(userRepository.findByUserId(userId)).thenReturn(user);
        when(bCryptPasswordEncoder.matches("currentPassword", user.getPassword())).thenReturn(true);
        when(bCryptPasswordEncoder.matches("newPassword", user.getPassword())).thenReturn(false);

        // When & Then
        assertThrows(UserModificationException.class, () -> userService.modifyUserPassword(userId, changePasswordDto));
    }



}