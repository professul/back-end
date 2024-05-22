package com.professul.professul.review.repository;

import com.professul.professul.review.entity.Professor;
import com.professul.professul.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long>, ProfessorCustom {
    @Query("SELECT MAX(profId) FROM Professor")
    int findMaxProfId();

    List<Professor> findByProfNameContainingOrderByProfName(String profName);

    List<Professor> findByUnivNameContainingOrderByProfName(String univName);

}