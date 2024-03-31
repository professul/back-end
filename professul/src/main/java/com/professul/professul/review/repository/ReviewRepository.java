package com.professul.professul.review.repository;

import com.professul.professul.review.dto.ReviewDTO;
import com.professul.professul.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("select sum(profRate) from Review where profId = :profId")
    int profRateSum(@Param("profId") int profId);

//    @Query("select count(profId) from Review where profId = :profId")
//    int profIdCount(@Param("profId") int profId);

    int countByProfId(@Param("profId") int profId);

    List<Review> findByProfId(@Param("profId") int profId);
}