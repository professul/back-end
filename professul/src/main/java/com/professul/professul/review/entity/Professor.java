package com.professul.professul.review.entity;

import com.professul.professul.review.dto.ProfessorDTO;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "professor")
@Getter
@DynamicInsert
public class Professor {
    @Id
    @Column(name = "prof_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int profId;

    @Column(name = "univ_id", nullable = false)
    private int univId;

    @Column(name = "dept_id", nullable = false)
    private int deptId;

    @Column(name = "prof_name", nullable = false)
    private String profName;

    @Column(name = "rating", nullable = false)
    private double rating;

    public void setProfessorDTO(ProfessorDTO pDTO) {
        this.profId = pDTO.getProfId();
        this.univId = pDTO.getUnivId();
        this.deptId = pDTO.getDeptId();
        this.profName = pDTO.getProfName();
        this.rating = pDTO.getRating();
    }
}
