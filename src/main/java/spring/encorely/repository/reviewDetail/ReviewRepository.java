package spring.encorely.repository.reviewDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.encorely.domain.reviewDetail.Review;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}