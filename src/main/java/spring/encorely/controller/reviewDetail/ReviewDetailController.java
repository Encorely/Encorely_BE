package spring.encorely.controller.reviewDetail;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.encorely.dto.reviewDetailDto.ReviewDetailRequestDto;
import spring.encorely.dto.reviewDetailDto.ReviewDetailResponseDto;
import spring.encorely.service.reviewDetailService.ReviewDetailService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewDetailController {

    private final ReviewDetailService reviewDetailService;

    // 특정 리뷰 상세 조회
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDetailResponseDto> getReviewDetail(@PathVariable Long reviewId) {
        ReviewDetailResponseDto reviewDto = reviewDetailService.getReviewDetailById(reviewId);
        return ResponseEntity.ok(reviewDto);
    }

    // 리뷰 수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDetailResponseDto> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewDetailRequestDto requestDto // 수정할 데이터 (새로운 내용)
    ) {
        ReviewDetailResponseDto updatedReview = reviewDetailService.updateReview(reviewId, requestDto.getUserId(), requestDto);
        return ResponseEntity.ok(updatedReview);
    }

    @PostMapping
    public ResponseEntity<ReviewDetailResponseDto> createReview(@Valid @RequestBody ReviewDetailRequestDto requestDto) {
        ReviewDetailResponseDto newReview = reviewDetailService.createReview(requestDto);
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