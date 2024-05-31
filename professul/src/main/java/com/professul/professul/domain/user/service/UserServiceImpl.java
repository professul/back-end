package com.professul.professul.domain.user.service;

import com.professul.professul.domain.user.dto.ChangePasswordDto;
import com.professul.professul.domain.user.dto.JoinDTO;
import com.professul.professul.domain.user.entity.Status;
import com.professul.professul.domain.user.entity.User;
import com.professul.professul.domain.user.entity.UserRole;
import com.professul.professul.domain.user.repository.UserRepository;
import com.professul.professul.exception.EmailAlreadyExistsException;
import com.professul.professul.exception.UserModificationException;
import com.professul.professul.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {


    private static final String USER_NOT_FOUND_MESSAGE = "사용자를 찾을 수 없습니다. ID: %s";
    private static final String PASSWORD_MISMATCH_MESSAGE = "현재 비밀번호가 일치하지 않습니다.";
    private static final String NEW_PASSWORD_SAME_AS_CURRENT_MESSAGE = "새 비밀번호는 기존의 비밀번호와 같을 수 없습니다.";
    private static final String PASSWORD_MISMATCH_CONFIRM_MESSAGE = "비밀번호가 일치하지 않습니다.";
    private static final String USER_MODIFICATION_ERROR_MESSAGE = "사용자 정보를 수정하는 과정에서 오류가 발생했습니다.";
    private static final String ALREADY_ACTIVATED_MESSAGE = "이미 활성화된 사용자입니다.";

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Transactional
    @Override
    public void joinProcess(JoinDTO joinDTO) throws EmailAlreadyExistsException { //회원가입
        String email = joinDTO.getEmail();
        String name = joinDTO.getName();

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("중복된 이메일입니다.");
        }

        String encryptedPassword = bCryptPasswordEncoder.encode(joinDTO.getPassword());

        User newUser = new User(null, email, name, encryptedPassword, Status.ACTIVE, UserRole.ROLE_USER);
        userRepository.save(newUser);

        log.info("회원가입 완료 - 이메일: {}", email);
    }

    private User getUserById(Long userId) throws UserNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, userId)));
    }


    private void saveUser(User user) throws UserModificationException {
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new UserModificationException(USER_MODIFICATION_ERROR_MESSAGE, e);
        }
    }


    @Transactional
    @Override
    public void modifyUserName(Long userId, String newName) throws UserNotFoundException, UserModificationException{
        User user = getUserById(userId);
        user.changeName(newName);
        saveUser(user);
        log.info("사용자 이름 변경 완료 - 사용자 ID: {}", userId);
    }


    @Transactional
    @Override
    public void modifyUserPassword(Long userId, ChangePasswordDto changePasswordDto) throws UserNotFoundException, UserModificationException {
        User user = getUserById(userId);
        validatePasswordChange(user, changePasswordDto);
        user.changePassword(changePasswordDto.getNewPassword(), bCryptPasswordEncoder);
        saveUser(user);
        log.info("비밀번호 변경 완료 - 사용자 ID: {}", userId);
    }

    private void validatePasswordChange(User user, ChangePasswordDto changePasswordDto) throws UserModificationException {
        if (!bCryptPasswordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())) {
            throw new UserModificationException(PASSWORD_MISMATCH_MESSAGE);
        }

        if (bCryptPasswordEncoder.matches(changePasswordDto.getNewPassword(), user.getPassword())) {
            throw new UserModificationException(NEW_PASSWORD_SAME_AS_CURRENT_MESSAGE);
        }

        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword())) {
            throw new UserModificationException(PASSWORD_MISMATCH_CONFIRM_MESSAGE);
        }
    }


    @Transactional
    @Override
    public User findUserById(Long userId) throws UserNotFoundException {
        return Optional.ofNullable(userRepository.findByUserId(userId))
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, userId)));
    }

    @Override
    public Boolean checkPassword(Long userId, String password) {
        User user = getUserById(userId);
        return user!=null && bCryptPasswordEncoder.matches(password, user.getPassword());
    }



    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public void activateUser(Long userId) {
        User user = getUserById(userId);
        if (user.getStatus() == Status.ACTIVE) {
            throw new IllegalStateException(ALREADY_ACTIVATED_MESSAGE);
        }
        user.activate();
        saveUser(user);
        log.info("사용자 활성화 - 사용자 ID: {}", userId);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public void suspendUser(Long userId, LocalDate until){
        User user = getUserById(userId);
        user.suspend(until);
        saveUser(user);
        log.info("사용자 정지 - 사용자 ID: {}, 정지 기간: {}", userId, until);

    }


    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public void banUser(Long userId, String reason) {
        User user = getUserById(userId);
        user.ban(reason);
        saveUser(user);
        log.info("사용자 차단 - 사용자 ID: {}, 사유: {}", userId, reason);
    }


    @Transactional
    @Override
    public void withdrawUser(Long userId) throws UserNotFoundException, UserModificationException { //사용자 탈퇴
        User user = getUserById(userId);
        user.withdraw();
        saveUser(user);
        log.info("사용자 탈퇴 - 사용자 ID: {}", userId);
    }

}
