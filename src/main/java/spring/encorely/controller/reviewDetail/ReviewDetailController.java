package spring.encorely.controller.reviewDetail;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import spring.encorely.domain.reviewDetail.ReviewDetail;
import spring.encorely.service.reviewDetailService.ReviewDetailService;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewDetailController {

    private final ReviewDetailService reviewDetailService;

    @GetMapping("/{id}")
    public ReviewDetail getReview(@PathVariable Long id) {
        return reviewDetailService.getReviewById(id);
    }

    // 좋아요 토글 요청: /api/reviews/1/like?userId=3
    @PostMapping("/{id}/like")
    public ReviewDetail likeOrUnlike(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        return reviewDetailService.toggleLike(id, userId);
    }
}
