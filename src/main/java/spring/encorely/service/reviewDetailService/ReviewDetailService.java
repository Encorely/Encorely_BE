package spring.encorely.service.reviewDetailService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.encorely.domain.reviewDetail.ReviewDetail;
import spring.encorely.domain.user.User;
import spring.encorely.repository.reviewDetail.ReviewDetailRepository;
import spring.encorely.repository.userRepository.UserRepository;

@Service
@RequiredArgsConstructor
public class ReviewDetailService {

    private final ReviewDetailRepository reviewDetailRepository;
    private final UserRepository userRepository;

    public ReviewDetail getReviewById(Long id) {
        return reviewDetailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다: " + id));
    }

    /**
     * 좋아요 토글 기능: 누르면 좋아요, 다시 누르면 취소
     */
    @Transactional
    public ReviewDetail toggleLike(Long reviewId, Long userId) {
        ReviewDetail review = getReviewById(reviewId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다: " + userId));

        if (review.isLikedBy(user)) {
            review.unlike(user);
        } else {
            review.like(user);
        }

        return review; // 변경된 좋아요 수 포함해서 반환
    }
}
