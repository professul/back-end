package com.professul.professul.review.dto;

import lombok.Data;

@Data
public class ProfessorDTO {
    private int profId; // 교수 ID
    private int univId; // 학교 ID
    private int deptId; // 학과 ID
    private String profName; // 교수 이름
    private double rating; // 평균 별점

//    public ProfessorDTO(int profId, int univId, int deptId, String profName, double rating) {
//        this.profId = profId;
//        this.univId = univId;
//        this.deptId = deptId;
//        this.profName = profName;
//        this.rating = rating;
//    }
}
