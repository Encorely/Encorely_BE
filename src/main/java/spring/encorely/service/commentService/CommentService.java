package spring.encorely.service.commentService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.encorely.domain.comment.Comment;
import spring.encorely.domain.reviewDetail.ReviewDetail;
import spring.encorely.domain.user.User;
import spring.encorely.dto.commentDto.CommentRequestDto;
import spring.encorely.dto.commentDto.CommentResponseDto;
import spring.encorely.exception.NotFoundException;
import spring.encorely.repository.comment.CommentRepository;
import spring.encorely.repository.reviewDetail.ReviewDetailRepository;
import spring.encorely.repository.userRepository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

        private final CommentRepository commentRepository; // 타입 대문자 시작 (PascalCase)
        private final ReviewDetailRepository reviewDetailRepository; // 타입 대문자 시작 (PascalCase)
        private final UserRepository userRepository;

        @Transactional(readOnly = true)
        public List<CommentResponseDto> getCommentsByReviewId(Long reviewId) {
            ReviewDetail review = reviewDetailRepository.findById(reviewId) // 변수 및 타입 대문자 시작 (PascalCase)
                    .orElseThrow(() -> new NotFoundException("Review not found with id: " + reviewId));

            List<Comment> topLevelComments = commentRepository.findByReviewAndParentIsNull(review); // 변수 및 타입 대문자 시작 (PascalCase)

            return topLevelComments.stream()
                    .map(CommentResponseDto::new) // 생성자 호출 시 타입 대문자 시작 (PascalCase)
                    .collect(Collectors.toList());
        }

        @Transactional
        public CommentResponseDto createComment(Long reviewId, CommentRequestDto requestDto) { // 파라미터 및 반환 타입 대문자 시작 (PascalCase)
            ReviewDetail review = reviewDetailRepository.findById(reviewId) // 변수 및 타입 대문자 시작 (PascalCase)
                    .orElseThrow(() -> new NotFoundException("Review not found with id: " + reviewId));
            User user = userRepository.findById(requestDto.getUserId())
                    .orElseThrow(() -> new NotFoundException("User not found with id: " + requestDto.getUserId()));

            Comment comment = Comment.builder() // 타입 및 빌더 호출 대문자 시작 (PascalCase)
                    .content(requestDto.getContent())
                    .review(review)
                    .user(user)
                    .build();

            Comment savedComment = commentRepository.save(comment); // 변수 및 타입 대문자 시작 (PascalCase)

            review.setCommentCount(review.getCommentCount() + 1);
            reviewDetailRepository.save(review); // 변수 및 타입 대문자 시작 (PascalCase)

            return new CommentResponseDto(savedComment);
        }

        @Transactional
        public CommentResponseDto createReply(Long reviewId, Long parentCommentId, CommentRequestDto requestDto) { // 파라미터 및 반환 타입 대문자 시작 (PascalCase)
            ReviewDetail review = reviewDetailRepository.findById(reviewId) // 변수 및 타입 대문자 시작 (PascalCase)
                    .orElseThrow(() -> new NotFoundException("Review not found with id: " + reviewId));
            Comment parentComment = commentRepository.findById(parentCommentId) // 변수 및 타입 대문자 시작 (PascalCase)
                    .orElseThrow(() -> new NotFoundException("Parent comment not found with id: " + parentCommentId));
            User user = userRepository.findById(requestDto.getUserId())
                    .orElseThrow(() -> new NotFoundException("User not found with id: " + requestDto.getUserId()));

            if (!parentComment.getReview().getId().equals(reviewId)) {
                throw new IllegalArgumentException("Parent comment does not belong to the specified review.");
            }

            Comment reply = Comment.builder() // 타입 및 빌더 호출 대문자 시작 (PascalCase)
                    .content(requestDto.getContent())
                    .parent(parentComment)
                    .review(review)
                    .user(user)
                    .build();

            parentComment.getChildren().add(reply);

            Comment savedReply = commentRepository.save(reply); // 변수 및 타입 대문자 시작 (PascalCase)

            review.setCommentCount(review.getCommentCount() + 1);
            reviewDetailRepository.save(review); // 변수 및 타입 대문자 시작 (PascalCase)

            return new CommentResponseDto(savedReply);
        }

        @Transactional
        public void deleteComment(Long reviewId, Long commentId, Long userId) {
            Comment comment = commentRepository.findById(commentId) // 변수 및 타입 대문자 시작 (PascalCase)
                    .orElseThrow(() -> new NotFoundException("Comment not found with id: " + commentId));

            if (!comment.getReview().getId().equals(reviewId)) {
                throw new IllegalArgumentException("Comment does not belong to the specified review.");
            }

            if (!comment.getUser().getId().equals(userId)) {
                throw new IllegalArgumentException("You are not authorized to delete this comment.");
            }

            ReviewDetail review = comment.getReview(); // 변수 및 타입 대문자 시작 (PascalCase)
            commentRepository.delete(comment); // 변수 및 타입 대문자 시작 (PascalCase)

            review.setCommentCount(review.getCommentCount() - 1);
            reviewDetailRepository.save(review); // 변수 및 타입 대문자 시작 (PascalCase)
        }
    }
