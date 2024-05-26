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
import com.professul.professul.review.entity.Review;
import com.professul.professul.review.repository.ReviewRepository;
import com.professul.professul.util.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Transactional
    @Override
    public void joinProcess(JoinDTO joinDTO) { //회원가입
        String email = joinDTO.getEmail();
        String name = joinDTO.getName();

        log.info("회원가입 요청 - 이메일: {}, 이름: {}", email, name);

        if (userRepository.existsByEmail(email)) {
            log.info("중복된 이메일입니다. 이메일: {}", email);
            throw new EmailAlreadyExistsException("중복된 이메일입니다.");
        }

        String encryptedPassword = bCryptPasswordEncoder.encode(joinDTO.getPassword());

        User newUser = new User(null, email, name, encryptedPassword, Status.ACTIVE, UserRole.ROLE_USER);
        userRepository.save(newUser);

        log.info("회원가입 완료 - 이메일: {}", email);
    }

    private User getUserById(Long userId){
        return userRepository.findById(userId).orElseThrow(()-> new UsernameNotFoundException("사용자를 찾을 수 없습니다. ID: " + userId));
    }

    private void saveUser(User user, String errorMessage) {
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new UserModificationException(errorMessage, e);
        }
    }

    @Override
    @Transactional
    public void modifyUserName(Long userId, String newName) {
        User user = getUserById(userId);
        user.changeName(newName);
        saveUser(user, "사용자 이름을 수정하는 과정에서 오류가 발생했습니다");
    }

    @Transactional
    @Override
    public void modifyUserPassword(Long userId, ChangePasswordDto changePasswordDto) throws UserModificationException {
        User user = getUserById(userId);

        if (!bCryptPasswordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())) {
            throw new UserModificationException("현재 비밀번호가 일치하지 않습니다.");
        }

        if (bCryptPasswordEncoder.matches(changePasswordDto.getNewPassword(), user.getPassword())) {
            throw new UserModificationException("새 비밀번호는 기존의 비밀번호와 같을 수 없습니다.");
        }

        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword())) {
            throw new UserModificationException("비밀번호가 일치하지 않습니다.");
        }

        user.changePassword(changePasswordDto.getNewPassword(), bCryptPasswordEncoder);
        saveUser(user, "사용자 비밀번호를 수정하는 과정에서 오류가 발생했습니다");
    }

    @Transactional
    @Override
    public User findUserById(Long userId) throws UserNotFoundException {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new UserNotFoundException("사용자를 찾을 수 없습니다. ID: " + userId);
        }
        return user;
    }


    @Override
    public Boolean checkPassword(Long userId, String password) {
        User user = userRepository.findByUserId(userId);
        return bCryptPasswordEncoder.matches(password, user.getPassword());
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public void activateUser(Long userId) throws Exception {
        User user = getUserById(userId);
        if (user.getStatus() == Status.ACTIVE) {
            throw new IllegalStateException("이미 활성화된 사용자입니다.");
        }
        user.activate();
        saveUser(user, "유저 활성화 실패: " + userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public void suspendUser(Long userId, LocalDate until) throws Exception {
        User user = getUserById(userId);
        user.suspend(until);
        saveUser(user, "유저 정지 실패: " + userId);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public void banUser(Long userId, String reason) {
        User user = getUserById(userId);
        user.ban(reason);
        saveUser(user, "유저 차단 실패: " + userId);
    }


    @Transactional
    @Override
    public void withdrawUser(Long userId) { //사용자 탈퇴
        User user = getUserById(userId);
        user.withdraw();
        saveUser(user, "사용자 탈퇴 중 오류가 발생했습니다");
    }

}
