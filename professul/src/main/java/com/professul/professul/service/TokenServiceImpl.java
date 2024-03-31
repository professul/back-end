package com.professul.professul.service;

import com.professul.professul.entity.RefreshEntity;
import com.professul.professul.jwt.JWTUtil;
import com.professul.professul.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    private final JWTUtil jwtUtil;

    private final RefreshRepository refreshRepository;

    public TokenServiceImpl(JWTUtil jwtUtil, RefreshRepository refreshRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshRepository=refreshRepository;
    }

    @Override
    public ResponseEntity<?> reissueToken(HttpServletRequest request, HttpServletResponse response) {
        log.info("토큰 재발급 요청 시작");
        String refresh = null;
        Cookie[] cookies = request.getCookies();

        if(cookies==null){
            log.warn("쿠키가 없습니다");
            return new ResponseEntity<>("No cookies found", HttpStatus.BAD_REQUEST);
        }
        for (Cookie cookie : cookies) {
            log.debug("쿠키 이름: {}, 값: {}", cookie.getName(), cookie.getValue());

            if (cookie.getName().equals("refresh")) {

                refresh = cookie.getValue();
                log.info("리프레시 토큰 발견: {}", refresh);

            }
        }

        if (refresh == null) {
            log.warn("리프레시 토큰이 null입니다.");

            //response status code
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
            log.info("리프레시 토큰 유효함");

        } catch (ExpiredJwtException e) {
            log.warn("리프레시 토큰이 만료됨");
            // 만료된 리프레시 토큰 제거
            refreshRepository.deleteByRefresh(refresh);

            //response status code
            // 401 Unauthorized 응답 반환 (클라이언트 측에서 로그아웃 처리)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // 토큰이 refresh 인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);
        log.debug("토큰 유형: {}", category);

        if (!"refresh".equals(category)) {
            log.warn("유효하지 않은 리프레시 토큰");

            //response status code
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        //DB에 저장되어 있는지 확인
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        log.debug("DB 내 리프레시 토큰 존재 여부: {}", isExist);

        if (!isExist) {
            log.warn("DB에 리프레시 토큰이 존재하지 않음");

            //response body
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);

        }

        String email = jwtUtil.getEmail(refresh);
        String role = jwtUtil.getRole(refresh);
        log.info("토큰 소유자: {}, 역할: {}", email, role);

        //make new JWT
        String newAccess = jwtUtil.createJwt("access", email, role, 600000L); //10분
        String newRefresh=jwtUtil.createJwt("refresh", email,role,86400000L); //24시간
        log.info("새 엑세스 토큰: {}", newAccess);
        log.info("새 리프레시 토큰: {}", newRefresh);

        //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        refreshRepository.deleteByRefresh(refresh);
        log.info("기존 리프레시 토큰 삭제됨: {}", refresh);

        addRefreshEntity(email, newRefresh, 86400000L);



        //response
        response.setHeader("access", newAccess);
        response.addHeader("Access-Control-Expose-Headers", "access");
        response.addCookie(createCookie("refresh", newRefresh));
        log.info("토큰 재발급 완료");

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Cookie createCookie(String key, String value){
        Cookie cookie=new Cookie(key,value);
        cookie.setMaxAge(24*60*60); //24시간
//        cookie.setSecure(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    private void addRefreshEntity(String email, String refresh, Long expiredMs){
        Date date= new Date(System.currentTimeMillis()+expiredMs);

        RefreshEntity refreshEntity= new RefreshEntity();
        refreshEntity.setEmail(email);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }
}


