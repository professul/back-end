package com.professul.professul.review.controller;

import com.professul.professul.review.dto.ProfessorDTO;
import com.professul.professul.review.dto.ReviewDTO;
import com.professul.professul.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReviewController {
    private final ReviewService rService;

    // 리뷰 등록
    @PostMapping("/rating")
    public ResponseEntity<String> registRating() throws Exception {
        log.debug("C: rating() 호출");

        ProfessorDTO pDTO = new ProfessorDTO();
        ReviewDTO rDTO = new ReviewDTO();

        try {
            // 교수 정보
            pDTO.setProfId(5);
            pDTO.setUnivId(1);
            pDTO.setDeptId(13);
            pDTO.setProfName("김연수");

            // 리뷰 정보
            rDTO.setUserId(1);
            rDTO.setProfId(5);
            rDTO.setClassLevel(5);
            rDTO.setGroupProject(4);
            rDTO.setClassPlan("일치");
            rDTO.setClassRate(3);
            rDTO.setProfRate(4);
            rDTO.setReview("좋아요");

            rService.registRating(pDTO, rDTO);

            return new ResponseEntity<>("ok", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
