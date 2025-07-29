package spring.encorely.repository.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.encorely.domain.comment.Comment;
import spring.encorely.domain.reviewDetail.Review; // ReviewDetail 임포트 확인

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByReviewDetailAndParentIsNull(Review reviewDetail);
}