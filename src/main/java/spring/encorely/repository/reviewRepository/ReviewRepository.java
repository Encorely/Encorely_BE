package spring.encorely.repository.reviewRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.encorely.domain.review.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
