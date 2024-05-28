package com.professul.professul.global.auth.controller;

import com.professul.professul.global.auth.jwt.JWTUtil;
import com.professul.professul.global.auth.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReissueController {
    private final TokenService tokenService;


    public ReissueController(TokenService tokenService) {
        this.tokenService=tokenService;
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        return tokenService.reissueToken(request, response);
    }
}


