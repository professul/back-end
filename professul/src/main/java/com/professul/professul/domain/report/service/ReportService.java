package com.professul.professul.domain.report.service;

import com.professul.professul.domain.report.entity.Report;
import com.professul.professul.domain.report.entity.Status;
import com.professul.professul.domain.user.entity.User;
import com.professul.professul.review.entity.Review;

public interface ReportService {
    Report reportReview(Long reviewId, Long userId);

    Report getReportById(Long reportId);

    void updateReportStatus(Long reportId, Status status);

    Report createReport(User reporter, User reportedUser, Review review, String reason);
}
