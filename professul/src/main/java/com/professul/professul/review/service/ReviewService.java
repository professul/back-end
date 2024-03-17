package com.professul.professul.review.service;

import com.professul.professul.review.dto.ProfessorDTO;
import com.professul.professul.review.dto.ReviewDTO;

public interface ReviewService {
    // 리뷰 등록
    public void registRating(ProfessorDTO pDTO, ReviewDTO rDTO) throws Exception;
}
