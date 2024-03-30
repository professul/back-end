package com.professul.professul.controller;

import com.professul.professul.jwt.JWTUtil;
import com.professul.professul.service.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReissueController {
    private final JWTUtil jwtUtil;
    private final TokenService tokenService;


    public ReissueController(JWTUtil jwtUtil,TokenService tokenService) {
        this.jwtUtil = jwtUtil;
        this.tokenService=tokenService;
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        return tokenService.reissueToken(request, response);
    }
}


