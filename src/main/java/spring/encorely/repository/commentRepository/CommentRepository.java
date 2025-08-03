package spring.encorely.repository.commentRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.encorely.domain.comment.Comment;
import spring.encorely.domain.review.Review;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByReviewId(Long reviewId);

    List<Comment> findByReviewIdAndParentIsNull(Long reviewId);
}