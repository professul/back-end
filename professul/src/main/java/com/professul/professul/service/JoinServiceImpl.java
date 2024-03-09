package com.professul.professul.service;

import com.professul.professul.dto.JoinDTO;
import com.professul.professul.entity.User;
import com.professul.professul.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinServiceImpl implements JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository=userRepository;
        this.bCryptPasswordEncoder=bCryptPasswordEncoder;
    }
    @Override
    public void joinProcess(JoinDTO joinDTO) throws Exception { //회원가입
        String email=joinDTO.getEmail();
        String password= joinDTO.getPassword();

        Boolean isExist=userRepository.existsByEmail(email);

        if(isExist){
            return;
        }

        User data= new User();
        data.setEmail(email);
        data.setPassword(bCryptPasswordEncoder.encode(password));
        data.setRole("ROLE_ADMIN");
        userRepository.save(data);
    }


}
