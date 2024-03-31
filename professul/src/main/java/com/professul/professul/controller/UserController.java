package com.professul.professul.controller;

import com.professul.professul.dto.JoinDTO;
import com.professul.professul.exception.EmailAlreadyExistsException;
import com.professul.professul.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService=userService;
    }

    @PostMapping("/join")
    public ResponseEntity<String> joinProcess(@RequestBody JoinDTO joinDTO) {
        try {
            userService.joinProcess(joinDTO);
            return ResponseEntity.ok("회원가입 성공");
        }catch (EmailAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용중인 이메일입니다");
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버오류발생");
        }
    }

}
