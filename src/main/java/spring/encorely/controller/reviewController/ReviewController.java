package spring.encorely.controller.reviewController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.encorely.apiPayload.ApiResponse;
import spring.encorely.domain.enums.ReviewImageCategory;
import spring.encorely.dto.reviewDto.ReviewRequestDTO;
import spring.encorely.dto.reviewDto.ReviewResponseDTO;
import spring.encorely.service.reviewService.ReviewImageService;
import spring.encorely.service.reviewService.ReviewService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewImageService reviewImageService;

    @PostMapping
    @Operation(summary = "후기 등록 API")
    public ApiResponse<ReviewResponseDTO.CreateReview> createReview(@AuthenticationPrincipal UserDetails userDetails,
                                                                    @Valid @RequestBody ReviewRequestDTO.CreateReview request) {
        return ApiResponse.onSuccess(reviewService.createReview(Long.parseLong(userDetails.getUsername()), request));
    }

    @DeleteMapping("/images/{imageId}")
    @Operation(summary = "후기 등록/수정 중 특정 이미지 삭제 API")
    public ApiResponse<String> deleteReviewImage(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long imageId) {
        reviewImageService.deleteReviewImage(Long.parseLong(userDetails.getUsername()), imageId);
        return ApiResponse.onSuccess("이미지가 삭제되었습니다.");
    }

    @GetMapping("/view")
    @Operation(summary = "공연장별 시야 후기 목록 조회 API")
    public ApiResponse<Page<ReviewResponseDTO.ViewReview>> getSeatReviewList(@RequestParam Long hallId,
                                                                             @RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "10") int size,
                                                                             @RequestParam(defaultValue = "createdAt,DESC") String sort,
                                                                             @RequestParam(defaultValue = "REVIEW") ReviewImageCategory category) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(
                Sort.Order.desc(sort.split(",")[0]) // createdAt,DESC
        ));

        Page<ReviewResponseDTO.ViewReview> response = reviewService.getSeatReviewList(hallId, category, pageable);
        return ApiResponse.onSuccess(response);
    }



}
