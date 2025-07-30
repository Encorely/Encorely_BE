package spring.encorely.service.commentService;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.encorely.domain.comment.Comment;
import spring.encorely.domain.reviewDetail.Review;
import spring.encorely.domain.user.User;
import spring.encorely.repository.comment.CommentRepository;
import spring.encorely.repository.reviewDetail.ReviewRepository;
import spring.encorely.repository.userRepository.UserRepository;


import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public Comment createComment(Long userId, Long reviewId, String content) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with ID: " + reviewId));

        Comment comment = Comment.builder()
                .content(content)
                .user(author)
                .review(review)
                .build();

        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsForReview(Long reviewId) {
        return commentRepository.findByReviewId(reviewId);
    }
}