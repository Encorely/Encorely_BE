package spring.encorely.controller.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.encorely.dto.commentDto.CommentRequestDto;
import spring.encorely.dto.commentDto.CommentResponseDto;
import spring.encorely.service.commentService.CommentService;

import jakarta.validation.Valid;

import java.util.List;


@RestController
@RequestMapping("/api/reviews/{reviewId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable Long reviewId) {
        List<CommentResponseDto> comments = commentService.getCommentsByReviewId(reviewId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(
                                                             @PathVariable Long reviewId,
                                                             @Valid @RequestBody CommentRequestDto requestDto
    ) {
        CommentResponseDto newComment = commentService.createComment(reviewId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newComment); // ApiResponse 제거
    }

    @PostMapping("/{parentCommentId}/replies")
    public ResponseEntity<CommentResponseDto> createReply(
                                                           @PathVariable Long reviewId,
                                                           @PathVariable Long parentCommentId,
                                                           @Valid @RequestBody CommentRequestDto requestDto
    ) {
        CommentResponseDto newReply = commentService.createReply(reviewId, parentCommentId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newReply);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
                                               @PathVariable Long reviewId,
                                               @PathVariable Long commentId,
                                               @RequestParam Long userId
    ) {
        commentService.deleteComment(reviewId, commentId, userId);
        return ResponseEntity.noContent().build();
    }
}