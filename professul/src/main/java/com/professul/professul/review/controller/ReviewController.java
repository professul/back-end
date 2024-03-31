package com.professul.professul.review.controller;

import com.professul.professul.review.dto.ProfessorDTO;
import com.professul.professul.review.dto.ReviewDTO;
import com.professul.professul.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/review")
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
            pDTO.setUnivName("서울대학교");
            pDTO.setDeptName("ㅇㅇ과");
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

    // 교수 한명 리뷰 조회
    @GetMapping("/5")
    public ResponseEntity<List<ReviewDTO>> getReview() throws Exception {
        log.debug("C: getReview() 호출");

        try {
            List<ReviewDTO> rList = rService.getReviewList(5);

            return new ResponseEntity<>(rList, HttpStatus.OK);
        } catch (Exception e) {
            log.debug("상세 리뷰 조회 불가", e);
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 교수 리스트 조회
    @GetMapping("/list")
    public ResponseEntity<List<ProfessorDTO>> getProfList(@RequestParam(name = "searchType", required = false) String searchType,
                                                          @RequestParam(name = "search", required = false) String search) throws Exception {
        log.debug("C: getProfList() 호출");

        try {
            List<ProfessorDTO> pList;

            if (searchType != null && search != null) {
                // 검색 O
                pList = rService.getProfList(searchType, search);
            } else {
                // 검색 X
                pList = rService.getProfList();
            }

            return new ResponseEntity<>(pList, HttpStatus.OK);
        } catch (Exception e) {
            log.debug("교수 리스트 조회 불가", e);
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
