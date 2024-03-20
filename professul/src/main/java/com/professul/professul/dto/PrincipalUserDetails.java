package com.professul.professul.dto;

import com.professul.professul.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class PrincipalUserDetails implements UserDetails {

    private final User user;
//    private final Map<String,Object> attribute;

    public PrincipalUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collections = new ArrayList<>();
        collections.add((GrantedAuthority) () -> user.getRole().name());
        return collections;
    }
//    public String getUserId(){return user.getUserId()+"";} //uesrId쓰는거
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
    public User getUser() {
        return this.user;
    }

}


