package com.professul.professul.review.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class ReviewDTO {
    private int userId;
    private int profId;
    private int classLevel;
    private int groupProject;
    private String classPlan;
    private int classRate;
    private int profRate;
    private String review;
    private String status;
}
