package spring.encorely.service.commentService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.encorely.domain.comment.Comment;
import spring.encorely.domain.reviewDetail.Review;
import spring.encorely.domain.user.User;
import spring.encorely.dto.commentDto.CommentRequestDto;
import spring.encorely.dto.commentDto.CommentResponseDto;
import spring.encorely.exception.NotFoundException;
import spring.encorely.repository.comment.CommentRepository;
import spring.encorely.repository.reviewDetail.ReviewRepository;
import spring.encorely.repository.userRepository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReviewRepository reviewDetailRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByReviewId(Long reviewId) {
        Review review = reviewDetailRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with id: " + reviewId));

        List<Comment> topLevelComments = commentRepository.findByReviewDetailAndParentIsNull(review); // ⭐ 수정

        return topLevelComments.stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponseDto createComment(Long reviewId, CommentRequestDto requestDto) {
        Review review = reviewDetailRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with id: " + reviewId));
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + requestDto.getUserId()));

        Comment comment = Comment.builder()
                .content(requestDto.getContent())
                .review(review)
                .user(user)
                .build();

        Comment savedComment = commentRepository.save(comment);

        review.setCommentCount(review.getCommentCount() + 1);
        reviewDetailRepository.save(review);

        return new CommentResponseDto(savedComment);
    }

    @Transactional
    public CommentResponseDto createReply(Long reviewId, Long parentCommentId, CommentRequestDto requestDto) {
        Review review = reviewDetailRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with id: " + reviewId));
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new NotFoundException("Parent comment not found with id: " + parentCommentId));
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + requestDto.getUserId()));

        if (!parentComment.getReview().getId().equals(reviewId)) {
            throw new IllegalArgumentException("Parent comment does not belong to the specified review.");
        }

        Comment reply = Comment.builder()
                .content(requestDto.getContent())
                .parent(parentComment)
                .review(review)
                .user(user)
                .build();

        parentComment.getChildren().add(reply);

        Comment savedReply = commentRepository.save(reply);

        review.setCommentCount(review.getCommentCount() + 1);
        reviewDetailRepository.save(review);

        return new CommentResponseDto(savedReply);
    }

    @Transactional
    public void deleteComment(Long reviewId, Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found with id: " + commentId));

        if (!comment.getReview().getId().equals(reviewId)) {
            throw new IllegalArgumentException("Comment does not belong to the specified review.");
        }

        if (!comment.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("You are not authorized to delete this comment.");
        }

        Review review = comment.getReview();
        commentRepository.delete(comment);

        review.setCommentCount(review.getCommentCount() - 1);
        reviewDetailRepository.save(review);
    }
}