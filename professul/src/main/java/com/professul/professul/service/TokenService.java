package com.professul.professul.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface TokenService {
    ResponseEntity<?> reissueToken(HttpServletRequest request, HttpServletResponse response);

}
