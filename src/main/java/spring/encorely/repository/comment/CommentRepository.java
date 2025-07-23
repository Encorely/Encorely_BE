package spring.encorely.repository.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.encorely.domain.comment.Comment; // 임포트 클래스 이름 'comment'에서 'Comment'로 수정됨
import spring.encorely.domain.reviewDetail.ReviewDetail; // 임포트 클래스 이름 'reviewDetail'에서 'ReviewDetail'로 수정됨
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> { // 타입 'comment'에서 'Comment'로 수정됨
    List<Comment> findByReviewAndParentIsNull(ReviewDetail review); // 타입 'comment'에서 'Comment'로, 'reviewDetail'에서 'ReviewDetail'로 수정됨
}