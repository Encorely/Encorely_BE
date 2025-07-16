package spring.encorely.controller.reviewController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.encorely.apiPayload.ApiResponse;
import spring.encorely.dto.reviewDto.ReviewRequestDTO;
import spring.encorely.dto.reviewDto.ReviewResponseDTO;
import spring.encorely.service.reviewService.ReviewService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @Operation(summary = "리뷰 등록 API")
    public ApiResponse<ReviewResponseDTO.CreateReview> createReview(@AuthenticationPrincipal UserDetails userDetails,
                                                                    @Valid @RequestBody ReviewRequestDTO.CreateReview request) {
        return ApiResponse.onSuccess(reviewService.createReview(Long.parseLong(userDetails.getUsername()), request));
    }
}
