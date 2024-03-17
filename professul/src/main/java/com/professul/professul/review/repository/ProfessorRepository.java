package com.professul.professul.review.repository;

import com.professul.professul.review.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long>, ProfessorCustom {
    @Query("SELECT MAX(profId) FROM Professor")
    int findMaxProfId();

}