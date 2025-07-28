package spring.encorely.repository.reviewDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spring.encorely.domain.reviewDetail.ReviewDetail;

import java.util.Optional;

public interface ReviewDetailRepository extends JpaRepository<ReviewDetail, Long> {
    @Query("SELECT rd FROM ReviewDetail rd JOIN FETCH rd.user u LEFT JOIN FETCH rd.images i WHERE rd.id = :reviewId")
    Optional<ReviewDetail> findByIdWithUserAndImages(@Param("reviewId") Long reviewId);
}
