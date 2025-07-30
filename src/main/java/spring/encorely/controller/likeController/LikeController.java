package spring.encorely.controller.likeController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import spring.encorely.service.likeService.LikeService;

import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    // 좋아요 토글 엔드포인트
    // POST /api/reviews/{reviewId}/like-toggle
    @PostMapping("/{reviewId}/like-toggle")
    public ResponseEntity<Map<String, Boolean>> toggleLike(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal UserDetails userDetails // 현재 로그인한 사용자 정보 (Spring Security)
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", false)); // 비인증 사용자 처리
        }

        Long userId = getUserIdFromUserDetails(userDetails); // 임시 구현

        try {
            boolean liked = likeService.toggleLike(userId, reviewId);
            return ResponseEntity.ok(Map.of("liked", liked)); // liked: true (좋아요 추가됨), false (좋아요 취소됨)
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", false)); // 리뷰 또는 사용자 없음
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", false)); // 기타 서버 오류
        }
    }

    // 사용자가 특정 리뷰에 좋아요를 눌렀는지 확인
    // GET /api/reviews/{reviewId}/has-liked
    @GetMapping("/{reviewId}/has-liked")
    public ResponseEntity<Map<String, Boolean>> checkUserLikeStatus(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("hasLiked", false)); // 비인증 사용자는 좋아요 누르지 않음으로 간주
        }
        Long userId = getUserIdFromUserDetails(userDetails); // 임시 구현

        try {
            boolean hasLiked = likeService.hasUserLiked(userId, reviewId);
            return ResponseEntity.ok(Map.of("hasLiked", hasLiked));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("hasLiked", false)); // 리뷰 또는 사용자 없음
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("hasLiked", false)); // 기타 서버 오류
        }
    }

    private Long getUserIdFromUserDetails(UserDetails userDetails) {

        return 1L;
    }
}