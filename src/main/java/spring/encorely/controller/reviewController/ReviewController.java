package spring.encorely.controller.reviewController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import spring.encorely.apiPayload.ApiResponse;
import spring.encorely.dto.commentDto.CommentRequestDto;
import spring.encorely.dto.commentDto.CommentResponseDto;
import spring.encorely.dto.reviewDto.ReviewRequestDTO;
import spring.encorely.dto.reviewDto.ReviewResponseDTO;
import spring.encorely.service.commentService.CommentService;
import spring.encorely.service.likeService.LikeService;
import spring.encorely.service.reviewService.ReviewImageService;
import spring.encorely.service.reviewService.ReviewService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewImageService reviewImageService;
    private final CommentService commentService;
    private final LikeService likeService;

    @PostMapping
    @Operation(summary = "후기 등록 API")
    public ApiResponse<ReviewResponseDTO.CreateReview> createReview(@AuthenticationPrincipal UserDetails userDetails,
                                                                    @Valid @RequestBody ReviewRequestDTO.CreateReview request) {
        return ApiResponse.onSuccess(reviewService.createReview(Long.parseLong(userDetails.getUsername()), request));
    }

    @DeleteMapping("/images/{imageId}")
    @Operation(summary = "후기 등록/수정 중 특정 이미지 삭제 API")
    public ApiResponse<String> deleteReviewImage(@PathVariable Long imageId) {
        reviewImageService.deleteReviewImage(imageId);
        return ApiResponse.onSuccess("이미지가 삭제되었습니다.");
    }

    // 특정 리뷰 상세 조회
    @GetMapping("/{reviewId}")
    @Operation(summary = "특정 리뷰 상세 조회(후기 전문)")
    public ApiResponse<ReviewResponseDTO.GetReview> getReviewDetail(@PathVariable Long reviewId) {
        return ApiResponse.onSuccess(reviewService.getReviewDetailById(reviewId));
    }

    // 리뷰 수정
    @PutMapping("/{reviewId}")
    @Operation(summary = "리뷰 수정")
    public ApiResponse<String> updateReview(@PathVariable Long reviewId, @Valid @RequestBody ReviewRequestDTO.UpdateReview request
    ) {
        reviewService.updateReview(reviewId, request);
        return ApiResponse.onSuccess("리뷰가 수정되었습니다.");
    }

    // 기존의 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    @Operation(summary = "리뷰 삭제")
    public ApiResponse<String> deleteReview(
            @PathVariable Long reviewId
    ) {
        reviewService.deleteReview(reviewId);
        return ApiResponse.onSuccess("리뷰가 삭제되었습니다.");
    }

    // 댓글 작성 엔드포인트
    // POST /api/reviews/{reviewId}/comments
    @PostMapping("/{reviewId}/comments")
    @Operation(summary = "댓글 추가")
    public ApiResponse<String> createComment(@PathVariable Long reviewId,
                                             @Valid @RequestBody CommentRequestDto.CreateComment request,
                                             @AuthenticationPrincipal UserDetails userDetails
    ) {
        commentService.createComment(Long.parseLong(userDetails.getUsername()), request, reviewId);
        return ApiResponse.onSuccess("댓글이 추가되었습니다.");
    }

    // 특정 리뷰의 댓글 목록 조회 엔드포인트
    // GET /api/reviews/{reviewId}/comments
    @GetMapping("/{reviewId}/comments")
    @Operation(summary = "특정 리뷰의 댓글 목록 조회")
    public ApiResponse<List<CommentResponseDto>> getComments(
            @PathVariable Long reviewId, @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ApiResponse.onSuccess(commentService.getCommentsForReview(reviewId, Long.parseLong(userDetails.getUsername())));
    }

    @PostMapping("/{reviewId}/like-toggle")
    @Operation(summary = "좋아요/좋아요 취소")
    public ApiResponse<Map<String, Boolean>> toggleLike(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal UserDetails userDetails // 현재 로그인한 사용자 정보 (Spring Security)
    ) {
        boolean liked = likeService.toggleLike(Long.parseLong(userDetails.getUsername()), reviewId);
        return ApiResponse.onSuccess(Map.of("liked", liked)); // liked: true (좋아요 추가됨), false (좋아요 취소됨)
    }

    // 사용자가 특정 리뷰에 좋아요를 눌렀는지 확인
    // GET /api/reviews/{reviewId}/has-liked
    @GetMapping("/{reviewId}/has-liked")
    @Operation(summary = "사용자가 특정 리뷰에 좋아요를 눌렀는지 확인")
    public ApiResponse<Map<String, Boolean>> checkUserLikeStatus(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        boolean hasLiked = likeService.hasUserLiked(Long.parseLong(userDetails.getUsername()), reviewId);
        return ApiResponse.onSuccess(Map.of("hasLiked", hasLiked));
    }

}
