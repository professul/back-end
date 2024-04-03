package com.professul.professul.global.auth.userDetails;

import com.professul.professul.domain.user.entity.User;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Getter
public class PrincipalUserDetails  implements UserDetails {

    private final User user;


    public PrincipalUserDetails(User user) {

        this.user = user;

    }

    public String getUserName(){
        return user.getName();

    }
    public Long getUserId(){
        return user.getUserId();
    }

    //해당 User의 권한 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collections = new ArrayList<>();
        collections.add((GrantedAuthority) () -> user.getRole().name());
        return collections;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }








}


