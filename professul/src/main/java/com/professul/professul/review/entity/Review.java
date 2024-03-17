package com.professul.professul.review.entity;

import com.professul.professul.review.dto.ReviewDTO;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "review")
@Getter
@DynamicInsert // null 값은 insert 쿼리에 포함되지 않게 세팅
@DynamicUpdate
public class Review {
    @Id
    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewId;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "prof_id", nullable = false)
    private int profId;

    @Column(name = "class_level", nullable = false)
    private int classLevel;

    @Column(name = "group_project", nullable = false)
    private int groupProject;

    @Column(name = "class_plan", nullable = false)
    private String classPlan;

    @Column(name = "class_rate", nullable = false)
    private int classRate;

    @Column(name = "prof_rate", nullable = false)
    private int profRate;

    @Column(name = "review", nullable = false)
    private String review;

    @Column(name = "status")
    private String status;

    public void setReviewDTO(ReviewDTO rDTO) {
        this.reviewId = rDTO.getReviewId();
        this.userId = rDTO.getUserId();
        this.profId = rDTO.getProfId();
        this.classLevel = rDTO.getClassLevel();
        this.groupProject = rDTO.getGroupProject();
        this.classPlan = rDTO.getClassPlan();
        this.classRate = rDTO.getClassRate();
        this.profRate = rDTO.getProfRate();
        this.review = rDTO.getReview();
        this.status = rDTO.getStatus();
    }
}
