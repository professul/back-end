package com.professul.professul.jwt;

import com.professul.professul.dto.PrincipalUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;
import java.util.Iterator;


public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;


    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil=jwtUtil;
    }
@Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //클라이언트 요청에서 email, password 추출
        String email = obtainUsername(request);
        String password = obtainPassword(request);


        System.out.println("여기 직전");
        if(email != null) {
            System.out.println(email);
        } else {
            System.out.println("email이 null입니다.");
        }
        //spring security에서 username과 password를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);
        //token에 담은 검증을 위한 AuthenticationManager로 전달
        return authenticationManager.authenticate(authToken);
    }

    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication){
    //유저 정보
    String email= authentication.getName();

    Collection<? extends GrantedAuthority> authorities=authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator=authorities.iterator();
        GrantedAuthority auth=iterator.next();
        String role=auth.getAuthority();

    //토큰 생성
        String access = jwtUtil.createJwt("access",email, role, 600000L);
        String refresh = jwtUtil.createJwt("refresh",email, role, 86400000L);

    //응답 설정
        response.setHeader("access", access);
        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());

//        PrincipalUserDetails principalUserDetails= (PrincipalUserDetails)authentication.getPrincipal();
//        String email= principalUserDetails.ge tUsername();
//
//        Collection<? extends GrantedAuthority> authorities=authentication.getAuthorities();
//        Iterator<? extends GrantedAuthority> iterator=authorities.iterator();
//        GrantedAuthority auth=iterator.next();
//
//        String role=auth.getAuthority();
//
//        String token= jwtUtil.createJwt(email,role,60*60*10L);
//        //Http 인증 방식은 RFC 7235 정의에 따라 이런 인증 헤더 형태를 가져야 한다
//        response.addHeader("Authorization", "Bearer " + token);

    }
    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed){
        response.setStatus(401);
        System.out.println("Login failed: " + failed.getMessage());
        failed.printStackTrace(); // 실패한 이유를 스택 트레이스로 출력



    }
    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}