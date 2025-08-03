package spring.encorely.repository.reviewRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.encorely.domain.review.Restaurant;
import spring.encorely.domain.review.Review;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findAllByReview(Review review);
}
