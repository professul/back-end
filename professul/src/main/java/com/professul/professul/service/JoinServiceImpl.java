package com.professul.professul.service;

import com.professul.professul.dto.JoinDTO;
import com.professul.professul.entity.User;
import com.professul.professul.repository.UserRepository;
import com.professul.professul.util.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JoinServiceImpl implements JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void joinProcess(JoinDTO joinDTO) throws Exception { //회원가입
        String email = joinDTO.getEmail();
        String name = joinDTO.getName();

        log.info("회원가입 요청 - 이메일: {}, 이름: {}", email, name);


        Boolean isExist = userRepository.existsByEmail(email);

        if (isExist) {
            log.info("중복된 이메일입니다. 이메일: {}", email);
            throw new Exception("중복된 이메일입니다");
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
        data.setRole(UserRole.ADMIN_ROLE);

        userRepository.save(data);

        log.info("회원가입 완료 - 이메일: {}", email);
    }


}
