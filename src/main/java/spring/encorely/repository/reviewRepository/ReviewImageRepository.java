package spring.encorely.repository.reviewRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.encorely.domain.enums.ReviewImageType;
import spring.encorely.domain.review.Facility;
import spring.encorely.domain.review.Restaurant;
import spring.encorely.domain.review.Review;
import spring.encorely.domain.review.ReviewImage;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
    Optional<ReviewImage> findByImageUrl(String imageUrl);
    List<ReviewImage> findAllByReviewAndTypeAndUsedIsTrue(Review review, ReviewImageType type);
    ReviewImage findByRestaurantAndUsedIsTrue(Restaurant restaurant);
    ReviewImage findByFacilityAndUsedIsTrue(Facility facility);
    List<ReviewImage> findAllByReviewAndUsedTrue(Review review);
}
