package com.professul.professul.domain.user.controller;

import com.professul.professul.domain.user.dto.*;
import com.professul.professul.domain.user.entity.UserRole;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

            if (modifyUserDto.getName() != null && !modifyUserDto.getName().isEmpty()) {
                user = userService.modifyUserName(userId, modifyUserDto.getName());
                responseDto.setName(user.getName());
            }

            return ResponseEntity.ok(responseDto);
        } catch (UserModificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }

    }


    @PutMapping("/user/change-password")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal PrincipalUserDetails principalUserDetails, @RequestBody ChangePasswordDto changePasswordDto, HttpServletRequest request, HttpServletResponse response) {
        Long userId = principalUserDetails.getUserId(); //유저아이디를 가져옴
        try {
            userService.modifyUserPassword(userId, changePasswordDto);
            tokenService.reissueToken(request, response);
            return ResponseEntity.ok().body("비밀번호가 변경되었습니다");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("비밀번호 변경 중 오류가 발생했습니다.");
        }
    }


//    @PostMapping("/user/delete/{userId}")
//    public ResponseEntity<?> withdrawUser(@PathVariable Long userId) throws Exception {
//        log.info("회원탈퇴 요청: {}", userId);
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserRole userRole = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ? UserRole.ROLE_ADMIN : UserRole.ROLE_USER;
//        userService.deactivateUser(userId, userRole);
//        return ResponseEntity.ok().build();
//    }

    @PostMapping("/user/delete/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> withdrawUser(@PathVariable Long userId) throws Exception {
        log.info("회원탈퇴 요청: {}", userId);
        userService.withdrawUser(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/admin/user/suspend/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> suspendUser(@PathVariable Long userId) throws Exception{
        log.info("회원정지요청:{}", userId);
        userService.suspendUser(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/admin/user/ban/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> banUser(@PathVariable Long userId) throws Exception{
        log.info("회원정지요청:{}", userId);
        userService.banUser(userId);
        return ResponseEntity.ok().build();

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