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

import javax.sound.sampled.ReverbType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProfessorRepository profRepository;
    private final EntityManager em;

    // 리뷰 등록
    @Override
    @Transactional
    public void registRating(ProfessorDTO pDTO, ReviewDTO rDTO) throws Exception {
//        log.debug("pDTO {}", pDTO);
//        log.debug("rDTO {}", rDTO);
        Professor prof = pDTO.toEntity();
        Review review = rDTO.toEntity();

        if(rDTO.getProfId() == 0) {
            // 신규 교수일 경우
            profRepository.save(prof); // 교수 정보 저장
            rDTO.setProfId(profRepository.findMaxProfId()); // 생성된 profId 설정
        }
        
        review.toReviewDTO();
        reviewRepository.save(review); // 리뷰 정보 저장

        // 교수 평균 별점 구하기
        int profId = pDTO.getProfId();
        int profRateSum = reviewRepository.profRateSum(profId);
        int profIdCount = reviewRepository.countByProfId(profId);
        double rateAvg = (double)profRateSum / profIdCount;

        // 평균 별점으로 update
        pDTO.setRating(rateAvg);
        prof = pDTO.toEntity();
        profRepository.save(prof); // 모든 컬럼이 업데이트 됨.. rating 컬럼만 하는 방법 찾기
    }

    // 교수 한명 리뷰 조회
    @Override
    public List<ReviewDTO> getReviewList(int profId) throws Exception {
        log.debug("S: getReviewList() 호출");
        // profId 사용해서 entity 가져오기
        List<Review> rList = reviewRepository.findByProfId(profId);

        // entity -> dto
        List<ReviewDTO> rDTOList = new ArrayList<>();
        for(Review review : rList) {
            rDTOList.add(review.toReviewDTO());
        }

        return rDTOList;
    }

    // 교수 리스트(검색 X)
    @Override
    public List<ProfessorDTO> getProfList() throws Exception {
        log.debug("S: getProfList() 호출, 검색 X");
        List<Professor> pList;
        List<ProfessorDTO> pDTOList = new ArrayList<>();

        pList = profRepository.findAll();

        // entity -> dto
        for(Professor professor : pList) {
            pDTOList.add(professor.toProfessorDTO());
        }

        return pDTOList;
    }

    // 교수 리스트(검색 O)
    @Override
    public List<ProfessorDTO> getProfList(String searchType, String search) throws Exception {
        log.debug("S: getProfList() 호출, 검색 O");
        log.debug("searchType, {}", searchType);
        log.debug("search, {}", search);
        List<Professor> pList;
        List<ProfessorDTO> pDTOList = new ArrayList<>();

        // 검색 o(교수명)
        if(searchType.equals("교수명")) {
            pList = profRepository.findByProfNameContaining(search);

            // entity -> dto
            for(Professor professor : pList) {
                pDTOList.add(professor.toProfessorDTO());
            }
        }

        // 검색 o(학교명)
        if(searchType.equals("학교명")) {
            log.debug("학교명 검색");
            pList = profRepository.findByUnivNameContaining(search);

            // entity -> dto
            for(Professor professor : pList) {
                pDTOList.add(professor.toProfessorDTO());
            }
        }

        return pDTOList;
    }
}
