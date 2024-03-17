package com.professul.professul.review.service;

import com.professul.professul.review.dto.ProfessorDTO;
import com.professul.professul.review.dto.ReviewDTO;
import com.professul.professul.review.entity.Professor;
import com.professul.professul.review.entity.Review;
import com.professul.professul.review.repository.ProfessorRepository;
import com.professul.professul.review.repository.ReviewRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProfessorRepository profRepository;
    private final EntityManager em;

    @Override
    @Transactional
    public void registRating(ProfessorDTO pDTO, ReviewDTO rDTO) throws Exception {
//        log.debug("pDTO {}", pDTO);
//        log.debug("rDTO {}", rDTO);
        Professor prof = new Professor();
        Review review = new Review();

        prof.setProfessorDTO(pDTO);

        if(rDTO.getProfId() == 0) {
            // 신규 교수일 경우
            profRepository.save(prof); // 교수 정보 저장
            rDTO.setProfId(profRepository.findMaxProfId()); // 생성된 profId 설정
        }
        
        review.setReviewDTO(rDTO);
        reviewRepository.save(review); // 리뷰 정보 저장

        // 교수 평균 별점 구하기
        int profId = pDTO.getProfId();
        int profRateSum = reviewRepository.profRateSum(profId);
        int profIdCount = reviewRepository.profIdCount(profId);
        double rateAvg = (double)profRateSum / profIdCount;

        // 평균 별점으로 update
        pDTO.setRating(rateAvg);
        prof.setProfessorDTO(pDTO);
        profRepository.save(prof); // 모든 컬럼이 업데이트 됨.. rating 컬럼만 하는 방법 찾기
    }
}
