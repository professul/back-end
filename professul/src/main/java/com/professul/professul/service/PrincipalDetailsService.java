package com.professul.professul.service;

import com.professul.professul.dto.PrincipalUserDetails;
import com.professul.professul.entity.User;
import com.professul.professul.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
@Slf4j
@Service //principalUserDetails에 account를 넣어주는 서비스
public class PrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    //ModelMapper 란 서로 다른 object 간의 필드 값을 자동으로 mapping 해주는 library

    public PrincipalDetailsService(UserRepository userRepository, ModelMapper mapper){
        this.userRepository=userRepository;
        this.mapper = mapper;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userData = userRepository.findByEmail(email);
        if(userData == null){
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        return new PrincipalUserDetails(userData);

    }
}
