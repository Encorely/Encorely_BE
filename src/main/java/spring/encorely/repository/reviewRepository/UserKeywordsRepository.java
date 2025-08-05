package spring.encorely.repository.reviewRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.encorely.domain.review.Restaurant;
import spring.encorely.domain.review.Review;
import spring.encorely.domain.review.UserKeywords;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserKeywordsRepository extends JpaRepository<UserKeywords, Long> {
    List<UserKeywords> findAllByReviewOrderByCreatedAtAsc(Review review);
    List<UserKeywords> findAllByRestaurantOrderByCreatedAtAsc(Restaurant restaurant);
    Optional<UserKeywords> findTop1ByReviewOrderByCreatedAtAsc(Review review);
}
