package spring.encorely.service.commentService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.encorely.apiPayload.code.status.ErrorStatus;
import spring.encorely.apiPayload.exception.handler.CommentHandler;
import spring.encorely.domain.comment.Comment;
import spring.encorely.domain.enums.NotificationType;
import spring.encorely.domain.review.Review;
import spring.encorely.domain.user.User;
import spring.encorely.dto.commentDto.CommentRequestDto;
import spring.encorely.dto.commentDto.CommentResponseDto;
import spring.encorely.repository.commentRepository.CommentRepository;
import spring.encorely.service.notificationService.NotificationService;
import spring.encorely.service.reviewService.ReviewService;
import spring.encorely.service.userService.UserService;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ReviewService reviewService;
    private final NotificationService notificationService;

    public Comment findById(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new CommentHandler(ErrorStatus.COMMENT_NOT_FOUND));
    }

    @Transactional
    public void createComment(Long userId, CommentRequestDto.CreateComment request, Long reviewId) {
        User author = userService.findById(userId);
        Review review = reviewService.findById(reviewId);
        Comment parent = findById(request.getParentId());

        Comment comment = Comment.builder()
                .parent(parent)
                .content(request.getContent())
                .user(author)
                .review(review)
                .build();

        commentRepository.save(comment);
        notificationService.createNotification(author, review, NotificationType.COMMENT, null);

    }

    public List<CommentResponseDto> getCommentsForReview(Long reviewId, Long currentUserId) {
        List<Comment> rootComments = commentRepository.findByReviewIdAndParentIsNull(reviewId);

        return rootComments.stream()
                .map(comment -> CommentResponseDto.fromEntity(comment, currentUserId))
                .collect(Collectors.toList());
    }

}