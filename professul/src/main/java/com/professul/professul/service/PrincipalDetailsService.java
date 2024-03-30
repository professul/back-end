package com.professul.professul.service;

import com.professul.professul.dto.PrincipalUserDetails;
import com.professul.professul.entity.User;
import com.professul.professul.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service //principalUserDetails에 account를 넣어주는 서비스
public class PrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public PrincipalDetailsService(UserRepository userRepository){
        this.userRepository=userRepository;
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
