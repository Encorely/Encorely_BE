package spring.encorely.repository.reviewDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spring.encorely.domain.reviewDetail.Review;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r " +
            "LEFT JOIN FETCH r.userKeywords uk " +
            "LEFT JOIN FETCH uk.keyword " +
            "WHERE r.id = :reviewId")
    Optional<Review> findByIdWithKeywords(@Param("reviewId") Long reviewId);

}