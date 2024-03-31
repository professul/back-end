package com.professul.professul.jwt;

import com.professul.professul.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;

    private final RefreshRepository refreshRepository;

    public CustomLogoutFilter(JWTUtil jwtUtil, RefreshRepository refreshRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        //path and method verify
        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^\\/logout$")) {
            log.info("로그아웃 경로가 아님, 요청을 계속 진행합니다."); // 경로 불일치 로그

            filterChain.doFilter(request, response);
            return;
        }
        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {

            filterChain.doFilter(request, response);
            return;
        }

        //get refresh token
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            log.info("쿠키가 없습니다."); // 쿠키 없음 로그
        } else {
            log.info("쿠키 개수: {}", cookies.length); // 쿠키 개수 로깅
        }
        String refresh = null;
// 쿠키 배열이 null이 아니면 반복문을 통해 "refresh" 쿠키 검색
        if (cookies != null) {

            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    refresh = cookie.getValue();
                    break; // "refresh" 쿠키를 찾으면 반복문 종료
                }
            }
        }

        if (refresh == null) {
            log.info("쿠키가 없습니다."); // 쿠키 없음 로그

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return; // 메소드 종료
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            log.warn("refresh 토큰이 만료되었습니다."); // 토큰 만료 로그
            //만료된 토큰 삭제
            refreshRepository.deleteByRefresh(refresh);

            //Refresh 토큰 Cookie 값 0
            Cookie cookie = new Cookie("refresh", null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);

            response.setStatus(HttpServletResponse.SC_OK);
            return; // 메소드 종료

//            //response status code
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            return;
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {

            //response status code
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //DB에 저장되어 있는지 확인
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {

            //response status code
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //로그아웃 진행
        //Refresh 토큰 DB에서 제거
        refreshRepository.deleteByRefresh(refresh);
// 로그인할때 refresh 토큰이 두개 발급되는 느낌
        //Refresh 토큰 Cookie 값 0
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
