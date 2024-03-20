package com.professul.professul.controller;

import com.professul.professul.dto.PrincipalUserDetails;
import com.professul.professul.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UserController {

    @GetMapping("/userInfo") //토큰에 따른 사용자 정보 가져오기
    public ResponseEntity<?> userInfo(Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            PrincipalUserDetails principalUserDetails = (PrincipalUserDetails) auth.getPrincipal();
            User user = principalUserDetails.getUser();

            log.info("유저정보 불러오기");
            log.info(user.toString());

            return ResponseEntity.ok(user);
        } else {
            log.error("인증되지 않은 사용자입니다");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증되지 않은사용자입니다");
        }

    }


}

