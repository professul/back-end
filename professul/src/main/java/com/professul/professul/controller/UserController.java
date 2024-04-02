package com.professul.professul.controller;

import com.professul.professul.dto.*;
import com.professul.professul.entity.User;
import com.professul.professul.exception.EmailAlreadyExistsException;
import com.professul.professul.exception.UserModificationException;
import com.professul.professul.service.UserService;
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

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/join")
    public ResponseEntity<String> joinProcess(@RequestBody JoinDTO joinDTO) {
        try {
            userService.joinProcess(joinDTO);
            return ResponseEntity.ok("회원가입 성공");
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용중인 이메일입니다");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버오류발생");
        }
    }

    @PatchMapping ("/user/modify")
    @Transactional
    public ResponseEntity<Object> modifyUser(@AuthenticationPrincipal PrincipalUserDetails principalUserDetails, @RequestBody ModifyUserDto modifyUserDto) throws Exception {
        Long userId= principalUserDetails.getUserId(); //유저아이디를 가져옴
        try{
        User modifiedUser= userService.modifyUser(userId, modifyUserDto);
        ModifyUserResponseDto responseDto= new ModifyUserResponseDto(modifiedUser.getName());
        return ResponseEntity.ok(responseDto);
        }catch (UserModificationException e){
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