package com.professul.professul.domain.report.service;

import com.professul.professul.domain.report.entity.Report;
import com.professul.professul.domain.report.entity.Status;
import com.professul.professul.domain.report.repository.ReportRepository;
import com.professul.professul.domain.user.entity.User;
import com.professul.professul.domain.user.repository.UserRepository;
import com.professul.professul.review.entity.Review;
import com.professul.professul.review.repository.ReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService{
    private final ReportRepository reportRepository;
    private final ReviewRepository reviewRepository;

    private final UserRepository userRepository;

    public ReportServiceImpl(ReportRepository reportRepository, ReviewRepository reviewRepository, UserRepository userRepository) {
        this.reportRepository = reportRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }


    @Transactional
    @Override
    public Report reportReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다."));


        return null;
    }

    private void validateReview(Review review, Long userId) throws Exception {
//        if (review.getUserId().equals(userId)) {
//            throw new Exception("자신의 리뷰를 신고할 수 없습니다.");
//        }
    }

    private Report createNewReport(Review review, Long userId) {
        Report report = new Report();
        return report;
    }

    @Transactional(readOnly = true)
    @Override
    public Report getReportById(Long reportId) { //신고 조회
        return reportRepository.findById(reportId).orElseThrow(()-> new IllegalArgumentException("Invalid report ID: " + reportId));
    }
    @Transactional
    @Override
    public void updateReportStatus(Long reportId, Status status) {

    }

    @Override
    public Report createReport(User reporter, User reportedUser, Review review, String reason) {
        return null;
    }
}
