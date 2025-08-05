package spring.encorely.repository.reviewRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.encorely.domain.enums.ReviewImageCategory;
import spring.encorely.domain.review.ReviewImage;

import java.util.Optional;

@Repository
public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
    Optional<ReviewImage> findByImageUrl(String imageUrl);
    Optional<ReviewImage> findTopByReviewIdAndUsedIsTrueOrderByCreatedAtAsc(Long reviewId);
    Optional<ReviewImage> findTopByReviewIdAndCategoryOrderByCreatedAtAsc(Long reviewId, ReviewImageCategory category);


}
