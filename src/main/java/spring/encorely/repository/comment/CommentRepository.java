package spring.encorely.repository.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.encorely.domain.comment.Comment;
import spring.encorely.domain.reviewDetail.ReviewDetail;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByReviewAndParentIsNull(ReviewDetail review);
}