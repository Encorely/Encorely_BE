package spring.encorely.service.reviewDetailService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.encorely.domain.reviewDetail.ReviewDetail;
import spring.encorely.domain.reviewDetail.ReviewImage;
import spring.encorely.domain.user.User;
import spring.encorely.dto.reviewDetailDto.ReviewDetailRequestDto;
import spring.encorely.dto.reviewDetailDto.ReviewDetailResponseDto;
import spring.encorely.exception.NotFoundException;
import spring.encorely.repository.reviewDetail.ReviewDetailRepository;
import spring.encorely.repository.reviewDetail.ReviewImageRepository;
import spring.encorely.repository.userRepository.UserRepository;

@Service
@RequiredArgsConstructor
public class ReviewDetailService {

    private final ReviewDetailRepository reviewDetailRepository;
    private final UserRepository userRepository;
    private final ReviewImageRepository reviewImageRepository;

    // 리뷰 생성 메서드
    @Transactional
    public ReviewDetailResponseDto createReview(ReviewDetailRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + requestDto.getUserId()));

        ReviewDetail newReview = ReviewDetail.builder()
                .hallId(requestDto.getHallId())
                .user(user)
                .showName(requestDto.getShowName())
                .artistName(requestDto.getArtistName())
                .seatArea(requestDto.getSeatArea())
                .seatRow(requestDto.getSeatRow())
                .seatNumber(requestDto.getSeatNumber())
                .rating(requestDto.getRating())
                .comment(requestDto.getComment())
                .detail(requestDto.getDetail())
                .seatDetail(requestDto.getSeatDetail())
                .showDate(requestDto.getShowDate())
                .round(requestDto.getRound())
                .viewCount(0)
                .likeCount(0)
                .commentCount(0)
                .scrapCount(0)
                .build();

        if (requestDto.getImageUrls() != null && !requestDto.getImageUrls().isEmpty()) {
            for (String imageUrl : requestDto.getImageUrls()) {
                ReviewImage reviewImage = ReviewImage.builder()
                        .imageUrl(imageUrl)
                        .build();
                newReview.addImage(reviewImage);
            }
        }

        ReviewDetail savedReview = reviewDetailRepository.save(newReview);
        return new ReviewDetailResponseDto(savedReview);
    }

    // 특정 리뷰 상세 조회 메서드
    @Transactional(readOnly = true)
    public ReviewDetailResponseDto getReviewDetailById(Long reviewId) {
        ReviewDetail review = reviewDetailRepository.findByIdWithUserAndImages(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with id: " + reviewId));
        return new ReviewDetailResponseDto(review);
    }

    // 리뷰 수정 메서드
    @Transactional
    public ReviewDetailResponseDto updateReview(Long reviewId, Long userId, ReviewDetailRequestDto requestDto) {
        ReviewDetail review = reviewDetailRepository.findByIdWithUserAndImages(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with id: " + reviewId));

        // 권한 확인
        if (!review.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("You are not authorized to update this review.");
        }

        // 기본 필드 업데이트
        review.setHallId(requestDto.getHallId());
        review.setShowName(requestDto.getShowName());
        review.setArtistName(requestDto.getArtistName());
        review.setSeatArea(requestDto.getSeatArea());
        review.setSeatRow(requestDto.getSeatRow());
        review.setSeatNumber(requestDto.getSeatNumber());
        review.setRating(requestDto.getRating());
        review.setComment(requestDto.getComment());
        review.setDetail(requestDto.getDetail());
        review.setSeatDetail(requestDto.getSeatDetail());
        review.setShowDate(requestDto.getShowDate());
        review.setRound(requestDto.getRound());
        review.getImages().clear();

        if (requestDto.getImageUrls() != null && !requestDto.getImageUrls().isEmpty()) {
            for (String imageUrl : requestDto.getImageUrls()) {
                ReviewImage reviewImage = ReviewImage.builder()
                        .imageUrl(imageUrl)
                        .build();
                review.addImage(reviewImage);
            }
        }

        ReviewDetail updatedReview = reviewDetailRepository.save(review);
        return new ReviewDetailResponseDto(updatedReview);
    }

    // 리뷰 삭제 메서드
    @Transactional
    public void deleteReview(Long reviewId, Long userId) {
        ReviewDetail review = reviewDetailRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with id: " + reviewId));

        if (!review.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("You are not authorized to delete this review.");
        }

        reviewDetailRepository.delete(review);
    }
}