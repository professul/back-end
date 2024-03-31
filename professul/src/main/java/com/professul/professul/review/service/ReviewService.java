package com.professul.professul.review.service;

import com.professul.professul.review.dto.ProfessorDTO;
import com.professul.professul.review.dto.ReviewDTO;

import java.util.List;

public interface ReviewService {
    // 리뷰 등록
    public void registRating(ProfessorDTO pDTO, ReviewDTO rDTO) throws Exception;

    // 교수 한명 리뷰 조회
    public List<ReviewDTO> getReviewList(int profId) throws Exception;

    // 교수 리스트(검색 X)
    public List<ProfessorDTO> getProfList() throws Exception;

    // 교수 리스트(검색 O)
    public List<ProfessorDTO> getProfList(String searchType, String search) throws Exception;
}
