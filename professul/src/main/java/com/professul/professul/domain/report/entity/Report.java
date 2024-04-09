package com.professul.professul.domain.report.entity;

import com.professul.professul.domain.user.entity.User;
import com.professul.professul.review.entity.Review;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Getter
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private User reporter; // 신고자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_user_id")
    private User reportedUser; // 신고당한 사용자(피신고자)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review; // 신고 관련 리뷰

    private String reason; //신고 사유

    @CreationTimestamp
    private Timestamp createDate; //신고 생성 시간

    @UpdateTimestamp
    private Timestamp resolvedDate; // 처리완료날짜

    @Enumerated(EnumType.STRING)
    private Status status=Status.PENDING; //신고 기본값



}
