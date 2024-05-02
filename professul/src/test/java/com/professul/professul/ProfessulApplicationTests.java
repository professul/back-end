package com.professul.professul;

import com.professul.professul.domain.report.entity.Report;
import com.professul.professul.domain.report.entity.Status;
import com.professul.professul.domain.report.service.ReportService;
import com.professul.professul.domain.user.entity.User;
import com.professul.professul.domain.user.repository.UserRepository;
import com.professul.professul.review.entity.Review;
import com.professul.professul.review.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProfessulApplicationTests {

    @Autowired
    private ReportService reportService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    void contextLoads() {
    }

    @Test
    public void testCreateReport(){
        User reporter = userRepository.findById(1L).get();
        User reportedUser = userRepository.findById(2L).get();
        Review review = reviewRepository.findById(1L).get();
        String reason="도배";

        Report report = reportService.createReport(reporter, reportedUser, review, reason);

        assertNotNull(report);
        assertEquals(reporter, report.getReporter());
        assertEquals(reportedUser, report.getReportedUser());
        assertEquals(review, report.getReview());
        assertEquals(reason, report.getReason());
        assertEquals(Status.PENDING, report.getStatus());


    }

    @Test
    public void testGetReportById() {
        Long reportId = 1L;

        Optional<Report> reportOptional = Optional.ofNullable(reportService.getReportById(reportId));

        assertTrue(reportOptional.isPresent());
        Report report = reportOptional.get();
        assertNotNull(report.getReporter());
        assertNotNull(report.getReportedUser());
        assertNotNull(report.getReview());
    }



}
