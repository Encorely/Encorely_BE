package spring.encorely.repository.reviewRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.encorely.domain.review.Review;
import spring.encorely.domain.user.User;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByUser (User user);
    List<Review> findAllByUserIdInOrderByCreatedAtDesc(List<Long> userIds);

}
