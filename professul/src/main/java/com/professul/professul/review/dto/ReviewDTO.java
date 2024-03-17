package com.professul.professul.review.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class ReviewDTO {
    private int reviewId; // 리뷰 ID
    private int userId; // 학생 ID
    private int profId; // 교수 ID
    private int classLevel; // 수업 난이도(1~5점)
    private int groupProject; // 조별 과제(1~5점)
    private String classPlan; // 강의계획서 일치도
    private int classRate; // 수업 평점(1~5점)
    private int profRate; // 교수 평점(1~5점)
    private String review; // 리뷰(한줄평)
    private String status; // 상태(정상/신고)
}
