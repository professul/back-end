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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        //이메일 중복검사
        Boolean isExist = userRepository.existsByEmail(email);

        if (isExist) {
            log.info("중복된 이메일입니다. 이메일: {}", email);
            throw new EmailAlreadyExistsException("중복된 이메일입니다.");
        }

        String password = joinDTO.getPassword();

        log.info("비밀번호 암호화 시작 - 이메일: {}", email);
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        log.info("비밀번호 암호화 완료 - 이메일: {}", email);

        log.info("새로운 회원 생성 - 이메일: {}, 이름: {}", email, name);
        User data = new User();
        data.setEmail(email);
        data.setName(name);
        data.setPassword(encryptedPassword);
        data.setRole(UserRole.ROLE_USER);
        data.setStatus(Status.ACTIVE);
        userRepository.save(data);

        log.info("회원가입 완료 - 이메일: {}", email);
    }

    @Override
    @Transactional
    public User modifyUserName(Long userId, String newName) throws UserModificationException {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new UserModificationException("사용자를 찾을 수 없습니다.");
        }

        user.setName(newName);
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void modifyUserPassword(Long userId, ChangePasswordDto changePasswordDto) throws UserModificationException {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new UserModificationException("사용자를 찾을 수 없습니다.");
        }

        if (!bCryptPasswordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())) {
            throw new UserModificationException("현재 비밀번호가 일치하지 않습니다.");
        }

        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword())) {
            throw new UserModificationException("비밀번호가 일치하지 않습니다");
        }

        user.setPassword(bCryptPasswordEncoder.encode(changePasswordDto.getNewPassword()));


        userRepository.save(user);
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
        String dbPassword = user.getPassword();
        return bCryptPasswordEncoder.matches(password, dbPassword);
    }
    @Transactional
    @Override
    public void deactivateUser(Long userId) throws Exception {

        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new UserNotFoundException("사용자를 찾을 수 없습니다. ID: " + userId);
        }
        user.setStatus(Status.CANCELED);
        userRepository.save(user);


    }


}
