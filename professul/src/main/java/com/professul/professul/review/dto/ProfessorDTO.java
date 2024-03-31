package com.professul.professul.review.dto;

import com.professul.professul.review.entity.Professor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfessorDTO {
    private int profId; // 교수 ID
    private String univName; // 학교 ID
    private String deptName; // 학과 ID
    private String profName; // 교수 이름
    private double rating; // 평균 별점

    public Professor toEntity(){
        return new Professor(
                profId,
                univName,
                deptName,
                profName,
                rating
        );
    }

    @Override
    public String toString() {
        return "ProfessorDTO{" +
                "profId=" + profId +
                ", univName=" + univName +
                ", deptName=" + deptName +
                ", profName='" + profName + '\'' +
                ", rating=" + rating +
                '}';
    }
}
