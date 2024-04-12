package com.professul.professul.domain.report.service;

import com.professul.professul.domain.report.entity.Report;
import com.professul.professul.domain.report.entity.Status;

public interface ReportService {
    Report reportReview(Long reviewId, Long userId);

    Report getReportById(Long reportId);

    void updateReportStatus(Long reportId, Status status);

}
