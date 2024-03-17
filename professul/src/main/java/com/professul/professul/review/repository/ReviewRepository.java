package com.professul.professul.review.repository;

import com.professul.professul.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("select sum(profRate) from Review where profId = :profId")
    int profRateSum(@Param("profId") int profId);

    @Query("select count(profId) from Review where profId = :profId")
    int profIdCount(@Param("profId") int profId);
}