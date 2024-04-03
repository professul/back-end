package com.professul.professul.domain.user.service;

import com.professul.professul.domain.user.dto.JoinDTO;
import com.professul.professul.domain.user.dto.ModifyUserDto;
import com.professul.professul.domain.user.entity.User;
import com.professul.professul.exception.EmailAlreadyExistsException;
import com.professul.professul.exception.UserModificationException;
import com.professul.professul.domain.user.repository.UserRepository;
import com.professul.professul.domain.user.entity.UserRole;
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
    public void joinProcess(JoinDTO joinDTO) throws Exception { //회원가입
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

        userRepository.save(data);

        log.info("회원가입 완료 - 이메일: {}", email);
    }

    @Override
    @Transactional
    public User modifyUser(Long userId, ModifyUserDto modifyUserDto) throws UserModificationException {
        User user= userRepository.findByUserId(userId);
        if (user == null) {
            throw new UserModificationException("사용자를 찾을 수 없습니다.");
        }

        //이름 변경
        if(modifyUserDto.getName()!=null && !modifyUserDto.getName().isEmpty()){
            user.setName(modifyUserDto.getName());
        }
        //비밀번호 변경
        if(modifyUserDto.getPassword()!=null && !modifyUserDto.getPassword().isEmpty()){
            String encodedPassword= bCryptPasswordEncoder.encode(modifyUserDto.getPassword());
            user.setPassword(encodedPassword);
        }

        return userRepository.save(user);
    }

    @Override
    public Boolean checkPassword(Long userId, String password) {
        User user= userRepository.findByUserId(userId);
        String dbPassword=user.getPassword();
        boolean match=bCryptPasswordEncoder.matches(password,dbPassword);
        return match;
    }


}
