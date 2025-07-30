package spring.encorely.service.reviewDetailService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.encorely.domain.hall.Hall;
import spring.encorely.domain.reviewDetail.Review;
import spring.encorely.domain.reviewDetail.ReviewCategory;
import spring.encorely.domain.reviewDetail.ReviewImage;
import spring.encorely.domain.reviewDetail.UserKeyword;

import spring.encorely.domain.user.User;
import spring.encorely.dto.hallDto.HallResponseDto;
import spring.encorely.dto.reviewDetailDto.ReviewRequestDto;
import spring.encorely.dto.reviewDetailDto.ReviewResponseDto;
import spring.encorely.exception.NotFoundException;
import spring.encorely.repository.reviewDetail.ReviewRepository;
import spring.encorely.repository.userRepository.UserRepository;
import spring.encorely.service.hallService.HallService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final HallService hallService;

    @Transactional
    public ReviewResponseDto createReview(ReviewRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + requestDto.getUserId()));

        Hall performanceHall = null;
        if (requestDto.getHallId() != null) {
            performanceHall = hallService.getHallEntityById(requestDto.getHallId());
        }

        Review newReview = Review.builder()
                .user(user)
                .reviewCategoryType(requestDto.getReviewCategoryType())
                .rating(requestDto.getRating())
                .comment(requestDto.getComment())
                .detail(requestDto.getDetail())
                .visitDate(requestDto.getVisitDate())

                // Performance Detail
                .performanceHall(performanceHall)
                .performanceShowName(requestDto.getPerformanceShowName())
                .performanceArtistName(requestDto.getPerformanceArtistName())
                .performanceSeatArea(requestDto.getPerformanceSeatArea())
                .performanceSeatRow(requestDto.getPerformanceSeatRow())
                .performanceSeatNumber(requestDto.getPerformanceSeatNumber())
                .performanceSeatDetail(requestDto.getPerformanceSeatDetail())
                .performanceShowDate(requestDto.getPerformanceShowDate())
                .performanceRound(requestDto.getPerformanceRound())

                // Restaurant Detail
                .restaurantPlaceName(requestDto.getRestaurantPlaceName())
                .restaurantCategory(requestDto.getRestaurantCategory())
                .restaurantAddress(requestDto.getRestaurantAddress())
                .restaurantLatitude(requestDto.getRestaurantLatitude())
                .restaurantLongitude(requestDto.getRestaurantLongitude())
                .restaurantBrandName(requestDto.getRestaurantBrandName())

                // Facility Detail
                .facilityType(requestDto.getFacilityType())
                .facilityTips(requestDto.getFacilityTips())
                .facilityCategory(requestDto.getFacilityCategory())
                .facilityAddress(requestDto.getFacilityAddress())
                .facilityLatitude(requestDto.getFacilityLatitude())
                .facilityLongitude(requestDto.getFacilityLongitude())
                .facilityConvenienceRating(requestDto.getFacilityConvenienceRating())
                .facilityCleanlinessRating(requestDto.getFacilityCleanlinessRating())
                .build();

        if (requestDto.getImageUrls() != null && !requestDto.getImageUrls().isEmpty()) {
            for (String imageUrl : requestDto.getImageUrls()) {
                ReviewImage reviewImage = ReviewImage.builder()
                        .imageUrl(imageUrl)
                        .build();
                newReview.addImage(reviewImage);
            }
        }

        if (requestDto.getKeywords() != null && !requestDto.getKeywords().isEmpty()) {
            for (String keywordText : requestDto.getKeywords()) {
                newReview.addUserKeyword(UserKeyword.builder().keywordText(keywordText).build());
            }
        }

        Review savedReview = reviewRepository.save(newReview);

        ReviewResponseDto responseDto = new ReviewResponseDto(savedReview);
        if (savedReview.getPerformanceHall() != null) {
            HallResponseDto hallResponseDto = hallService.getHallById(savedReview.getPerformanceHall().getId());
            responseDto.setHallInfo(hallResponseDto);
        }

        return responseDto;
    }

    @Transactional
    public ReviewResponseDto getReviewDetailById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with id: " + reviewId));

        review.setViewCount(review.getViewCount() + 1);

        ReviewResponseDto responseDto = new ReviewResponseDto(review);
        if (review.getPerformanceHall() != null) {
            HallResponseDto hallResponseDto = hallService.getHallById(review.getPerformanceHall().getId());
            responseDto.setHallInfo(hallResponseDto);
        }

        return responseDto;
    }

    @Transactional
    public ReviewResponseDto updateReview(Long reviewId, Long userId, ReviewRequestDto requestDto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with id: " + reviewId));

        if (!review.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("You are not authorized to update this review.");
        }

        review.setRating(requestDto.getRating());
        review.setComment(requestDto.getComment());
        review.setDetail(requestDto.getDetail());
        review.setVisitDate(requestDto.getVisitDate());

        review.getImages().clear();
        if (requestDto.getImageUrls() != null && !requestDto.getImageUrls().isEmpty()) {
            for (String imageUrl : requestDto.getImageUrls()) {
                ReviewImage reviewImage = ReviewImage.builder().imageUrl(imageUrl).build();
                review.addImage(reviewImage);
            }
        }

        review.getUserKeywords().clear();
        if (requestDto.getKeywords() != null && !requestDto.getKeywords().isEmpty()) {
            for (String keywordText : requestDto.getKeywords()) {
                review.addUserKeyword(UserKeyword.builder().keywordText(keywordText).build());
            }
        }

        Hall performanceHall = null;
        if (requestDto.getHallId() != null) {
            performanceHall = hallService.getHallEntityById(requestDto.getHallId());
        }
        review.setPerformanceHall(performanceHall);
        review.setPerformanceShowName(requestDto.getPerformanceShowName());
        review.setPerformanceArtistName(requestDto.getPerformanceArtistName());
        review.setPerformanceSeatArea(requestDto.getPerformanceSeatArea());
        review.setPerformanceSeatRow(requestDto.getPerformanceSeatRow());
        review.setPerformanceSeatNumber(requestDto.getPerformanceSeatNumber());
        review.setPerformanceSeatDetail(requestDto.getPerformanceSeatDetail());
        review.setPerformanceShowDate(requestDto.getPerformanceShowDate());
        review.setPerformanceRound(requestDto.getPerformanceRound());

        review.setRestaurantPlaceName(requestDto.getRestaurantPlaceName());
        review.setRestaurantCategory(requestDto.getRestaurantCategory());
        review.setRestaurantAddress(requestDto.getRestaurantAddress());
        review.setRestaurantLatitude(requestDto.getRestaurantLatitude());
        review.setRestaurantLongitude(requestDto.getRestaurantLongitude());
        review.setRestaurantBrandName(requestDto.getRestaurantBrandName());

        review.setFacilityType(requestDto.getFacilityType());
        review.setFacilityTips(requestDto.getFacilityTips());
        review.setFacilityCategory(requestDto.getFacilityCategory());
        review.setFacilityAddress(requestDto.getFacilityAddress());
        review.setFacilityLatitude(requestDto.getFacilityLatitude());
        review.setFacilityLongitude(requestDto.getFacilityLongitude());
        review.setFacilityConvenienceRating(requestDto.getFacilityConvenienceRating());
        review.setFacilityCleanlinessRating(requestDto.getFacilityCleanlinessRating());

        Review updatedReview = reviewRepository.save(review);

        ReviewResponseDto responseDto = new ReviewResponseDto(updatedReview);
        if (updatedReview.getPerformanceHall() != null) {
            HallResponseDto hallResponseDto = hallService.getHallById(updatedReview.getPerformanceHall().getId());
            responseDto.setHallInfo(hallResponseDto);
        }

        return responseDto;
    }

    @Transactional
    public void deleteReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with id: " + reviewId));

        if (!review.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("You are not authorized to delete this review.");
        }

        reviewRepository.delete(review);
    }
}