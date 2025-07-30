package spring.encorely.controller.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import spring.encorely.domain.comment.Comment;
import spring.encorely.service.commentService.CommentService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성 요청
    @Data // Lombok
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentRequest {
        private String content;
    }

    // 댓글 작성 엔드포인트
    // POST /api/reviews/{reviewId}/comments
    @PostMapping("/{reviewId}/comments")
    public ResponseEntity<Comment> createComment(
            @PathVariable Long reviewId,
            @RequestBody CommentRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = getUserIdFromUserDetails(userDetails); // 임시 구현

        try {
            Comment newComment = commentService.createComment(userId, reviewId, request.getContent());
            return ResponseEntity.status(HttpStatus.CREATED).body(newComment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 리뷰 또는 사용자 없음
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 특정 리뷰의 댓글 목록 조회 엔드포인트
    // GET /api/reviews/{reviewId}/comments
    @GetMapping("/{reviewId}/comments")
    public ResponseEntity<List<Comment>> getComments(
            @PathVariable Long reviewId
    ) {
        try {
            List<Comment> comments = commentService.getCommentsForReview(reviewId);
            return ResponseEntity.ok(comments);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 리뷰 없음
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private Long getUserIdFromUserDetails(UserDetails userDetails) {
        return 1L; // 임시 userId
    }
}