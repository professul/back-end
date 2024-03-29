package com.professul.professul.controller;

import com.professul.professul.dto.PrincipalUserDetails;
import com.professul.professul.entity.Report;
import com.professul.professul.repository.ReportRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

//    @GetMapping("/admin")
//    @PreAuthorize("hasRole('ADMIN')") // 2. 메서드 단위 보안 설정
//    public String adminPage(PrincipalUserDetails principal) {
//
//        return "Admin Page";
//    }
@RestController
public class AdminController {

    private final ReportRepository reportRepository;

    public AdminController(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @GetMapping("/admin")
    public ResponseEntity<List<Report>> getAllReports() {
        List<Report> reports = reportRepository.findAll();
        return ResponseEntity.ok(reports); // HTTP 응답으로 신고 목록을 반환
    }
}

