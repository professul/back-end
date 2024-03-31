package com.professul.professul.review.entity;

import com.professul.professul.review.dto.ReviewDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "review")
@Getter
@DynamicInsert // null 값은 insert 쿼리에 포함되지 않게 세팅
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public Review(int reviewId, int userId, int profId, int classLevel, int groupProject, String classPlan, int classRate, int profRate, String review, String status) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.profId = profId;
        this.classLevel = classLevel;
        this.groupProject = groupProject;
        this.classPlan = classPlan;
        this.classRate = classRate;
        this.profRate = profRate;
        this.review = review;
        this.status = status;
    }

    public ReviewDTO toReviewDTO() {
        return ReviewDTO.builder()
                .reviewId(reviewId)
                .userId(userId)
                .profId(profId)
                .classLevel(classLevel)
                .groupProject(groupProject)
                .classPlan(classPlan)
                .classRate(classRate)
                .profRate(profRate)
                .review(review)
                .status(status)
                .build();
    }
}
