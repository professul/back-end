package com.professul.professul.review.service;

import com.professul.professul.review.dto.ReviewDTO;
import com.professul.professul.review.entity.Review;
import com.professul.professul.review.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void registRating(ReviewDTO rDTO) throws Exception {
        log.debug("rDTO {}", rDTO);
        Review review = new Review();

        review.setUserId(rDTO.getUserId());
        review.setProfId(rDTO.getProfId());
        review.setClassLevel(rDTO.getClassLevel());
        review.setGroupProject(rDTO.getGroupProject());
        review.setClassPlan(rDTO.getClassPlan());
        review.setClassRate(rDTO.getClassRate());
        review.setProfRate(rDTO.getProfRate());
        review.setReview(rDTO.getReview());

        // 교수 새로 등록(rDTO.getProfId() == null 일 때?)

        // 리뷰 저장
        reviewRepository.save(review);
    }
}
