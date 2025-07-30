package spring.encorely.repository.like;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.encorely.domain.like.Like;
import spring.encorely.domain.reviewDetail.Review;
import spring.encorely.domain.user.User;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    // 특정 사용자가 특정 리뷰에 좋아요를 눌렀는지 확인
    Optional<Like> findByUserAndReview(User user, Review review);

}