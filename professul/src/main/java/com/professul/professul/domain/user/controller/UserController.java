package com.professul.professul.domain.user.controller;

import com.professul.professul.domain.user.dto.*;
import com.professul.professul.dto.*;
import com.professul.professul.domain.user.entity.User;
import com.professul.professul.exception.EmailAlreadyExistsException;
import com.professul.professul.exception.UserModificationException;
import com.professul.professul.domain.user.service.UserService;
import com.professul.professul.domain.user.dto.JoinDTO;
import com.professul.professul.global.auth.service.TokenService;
import com.professul.professul.global.auth.userDetails.PrincipalUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class UserController {

    private final UserService userService;

    private final TokenService tokenService;

    public UserController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostMapping("/join")
    public ResponseEntity<String> joinProcess(@RequestBody JoinDTO joinDTO) {
        try {
            userService.joinProcess(joinDTO);
            return ResponseEntity.ok("회원가입 성공");
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용중인 이메일입니다");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버오류발생");
        }
    }

    @PostMapping("/user/checkPassword") //비밀번호 확인
    public ResponseEntity<?> checkPassword(@AuthenticationPrincipal PrincipalUserDetails principalUserDetails, @RequestBody CheckPasswordDto checkPasswordDto) {
        log.info("비밀번호 확인 진입");
        Long userId = principalUserDetails.getUserId();
        boolean match = userService.checkPassword(userId, checkPasswordDto.getPassword());
        if (match) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 일치하지 않습니다");
        }
    }


    @PatchMapping("/user/modify")
    public ResponseEntity<Object> modifyUser(@AuthenticationPrincipal PrincipalUserDetails principalUserDetails, @RequestBody ModifyUserDto modifyUserDto, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long userId = principalUserDetails.getUserId(); //유저아이디를 가져옴
        try {
            User user = userService.findUserById(userId);
            ModifyUserResponseDto responseDto = new ModifyUserResponseDto(null);
            boolean isPasswordChanged = false;

            //이름 변경
            if (modifyUserDto.getName() != null && !modifyUserDto.getName().isEmpty()) {
                user = userService.modifyUserName(userId, modifyUserDto.getName());
                responseDto.setName(user.getName());
            }

            // 비밀번호 변경
            if (modifyUserDto.getPassword() != null && !modifyUserDto.getPassword().isEmpty()) {
                user = userService.modifyUserPassword(userId, modifyUserDto.getPassword());
                isPasswordChanged = true;
            }

            // 비밀번호가 변경되었다면 토큰 재발급
            if (isPasswordChanged) {
                tokenService.reissueToken(request, response);
            }

            return ResponseEntity.ok(responseDto);
        } catch (UserModificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }

    }

    @GetMapping("/user/info")
    public ResponseEntity<UserInfoResponse> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            log.info("Authentication 객체: {}", authentication);
            Object principal = authentication.getPrincipal();
            log.info("Principal 객체: {}", principal);
            if (principal instanceof PrincipalUserDetails) {
                PrincipalUserDetails userDetails = (PrincipalUserDetails) principal;
                UserInfoResponse userInfoResponse = new UserInfoResponse(userDetails.getUserName(), userDetails.getUserId());
                return ResponseEntity.ok(userInfoResponse);
            } else {
                log.info("Principal이 PrincipalUserDetails 인스턴스가 아님");
            }
        } else {
            log.info("Authentication 객체가 null");
        }
        return ResponseEntity.badRequest().body(null);
    }

}