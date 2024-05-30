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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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
    public ResponseEntity<String> joinProcess(@RequestBody JoinDTO joinDTO) throws Exception {
        userService.joinProcess(joinDTO);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/user/checkPassword") //비밀번호 확인
    public ResponseEntity<?> checkPassword(@AuthenticationPrincipal PrincipalUserDetails principalUserDetails, @RequestBody CheckPasswordDto checkPasswordDto) {
        Long userId = principalUserDetails.getUserId();
        boolean match = userService.checkPassword(userId, checkPasswordDto.getPassword());
        if (match) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 일치하지 않습니다");
        }
    }

    @PatchMapping("/user/modify")
    public ResponseEntity<?> modifyUser(@AuthenticationPrincipal PrincipalUserDetails principalUserDetails, @RequestBody ModifyUserDto modifyUserDto) {
        Long userId = principalUserDetails.getUserId();
        try {
            userService.modifyUserName(userId, modifyUserDto.getName());
            ModifyUserResponseDto responseDto = new ModifyUserResponseDto(modifyUserDto.getName());
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }


    @PutMapping("/user/change-password")
    public ResponseEntity<String> changePassword(@AuthenticationPrincipal PrincipalUserDetails principalUserDetails, @RequestBody ChangePasswordDto changePasswordDto, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long userId = principalUserDetails.getUserId();
        userService.modifyUserPassword(userId, changePasswordDto);
        tokenService.reissueToken(request, response);
        return ResponseEntity.ok("비밀번호가 변경되었습니다");
    }


    @PostMapping("/user/delete/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> withdrawUser(@PathVariable Long userId) {
        log.info("회원탈퇴 요청: {}", userId);
        try {
            userService.withdrawUser(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("회원탈퇴 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원탈퇴 중 오류가 발생했습니다.");
        }
    }


    @PostMapping("/admin/suspend/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> suspendUser(@PathVariable Long userId, @RequestParam("until") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate until) {
        try {
            userService.suspendUser(userId, until);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("회원 정지 중 오류가 발생했습니다."));
        }
    }

    @PostMapping("/admin/ban/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> banUser(@PathVariable Long userId, @RequestParam("reason") String reason) throws Exception{
        log.info("회원 차단 요청: {}, 사유: {}", userId, reason);
        try {
            userService.banUser(userId, reason);
            return ResponseEntity.ok().build();
        }catch (UsernameNotFoundException e){
            log.error("사용자 찾기 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }catch (Exception e){
            log.error("회원 차단 중 오류 발생",e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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