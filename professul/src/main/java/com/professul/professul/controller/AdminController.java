package com.professul.professul.controller;

import com.professul.professul.entity.Report;
import com.professul.professul.repository.ReportRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class AdminController {

    private final ReportRepository reportRepository;

    public AdminController(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @GetMapping("/admin")
    public ResponseEntity<List<Report>> getAllReports() {
        List<Report> reports = reportRepository.findAll();
        return ResponseEntity.ok(reports);
    }
}

