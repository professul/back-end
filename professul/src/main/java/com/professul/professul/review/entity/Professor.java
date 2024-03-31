package com.professul.professul.review.entity;

import com.professul.professul.review.dto.ProfessorDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "professor")
@Getter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Professor {
    @Id
    @Column(name = "prof_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int profId;

    @Column(name = "univ_name", nullable = false)
    private String univName;

    @Column(name = "dept_name", nullable = false)
    private String deptName;

    @Column(name = "prof_name", nullable = false)
    private String profName;

    @Column(name = "rating", nullable = false)
    private double rating;

    public Professor(int profId, String univName, String deptName, String profName, double rating) {
        this.profId = profId;
        this.univName = univName;
        this.deptName = deptName;
        this.profName = profName;
        this.rating = rating;
    }

    @Builder
    public ProfessorDTO toProfessorDTO() {
        return ProfessorDTO.builder()
                .profId(profId)
                .univName(univName)
                .profName(profName)
                .profName(profName)
                .rating(rating)
                .build();
    }
}
