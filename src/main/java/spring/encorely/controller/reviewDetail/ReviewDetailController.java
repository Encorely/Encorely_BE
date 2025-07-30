package spring.encorely.controller.reviewDetail;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.encorely.dto.reviewDetailDto.ReviewRequestDto;
import spring.encorely.dto.reviewDetailDto.ReviewResponseDto;
import spring.encorely.service.reviewDetailService.ReviewService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewDetailController {

    private final ReviewService reviewDetailService;

    // 특정 리뷰 상세 조회
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> getReviewDetail(@PathVariable Long reviewId) {
        ReviewResponseDto reviewDto = reviewDetailService.getReviewDetailById(reviewId);
        return ResponseEntity.ok(reviewDto);
    }

    // 리뷰 수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequestDto requestDto
    ) {
        ReviewResponseDto updatedReview = reviewDetailService.updateReview(reviewId, requestDto.getUserId(), requestDto);
        return ResponseEntity.ok(updatedReview);
    }

    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(@Valid @RequestBody ReviewRequestDto requestDto) {
        ReviewResponseDto newReview = reviewDetailService.createReview(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newReview);
    }

    // 기존의 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId,
            @RequestParam Long userId
    ) {
        reviewDetailService.deleteReview(reviewId, userId);
        return ResponseEntity.noContent().build();
    }
}