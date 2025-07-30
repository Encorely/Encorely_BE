package spring.encorely.service.likeService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.encorely.domain.like.Like;
import spring.encorely.domain.reviewDetail.Review;
import spring.encorely.domain.user.User;
import spring.encorely.repository.like.LikeRepository;
import spring.encorely.repository.reviewDetail.ReviewRepository;
import spring.encorely.repository.userRepository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository; // 사용자 리포지토리
    private final ReviewRepository reviewRepository; // 후기 리포지토리

    @Transactional
    public boolean toggleLike(Long userId, Long reviewId) {
        // 사용자 및 리뷰 엔티티 조회 (없으면 예외 발생)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with ID: " + reviewId));

        // 이미 좋아요를 눌렀는지 확인
        Optional<Like> existingLike = likeRepository.findByUserAndReview(user, review);

        if (existingLike.isPresent()) {
            // 이미 좋아요를 눌렀다면 취소
            likeRepository.delete(existingLike.get());
            review.decrementLikeCount(); ;
            return false; // 좋아요 취소

        } else {
            // 좋아요를 누르지 않았다면 추가
            Like newLike = new Like(user, review);
            likeRepository.save(newLike);
            review.incrementLikeCount();
            return true; // 좋아요 추가됨
        }
    }

    public boolean hasUserLiked(Long userId, Long reviewId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with ID: " + reviewId));
        return likeRepository.findByUserAndReview(user, review).isPresent();
    }
}