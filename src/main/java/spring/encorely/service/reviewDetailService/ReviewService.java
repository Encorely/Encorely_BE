package spring.encorely.service.reviewDetailService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.encorely.domain.hall.Hall;
import spring.encorely.domain.reviewDetail.*;
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

        Review newReview = Review.builder()
                .user(user)
                .reviewCategoryType(requestDto.getReviewCategoryType())
                .rating(requestDto.getRating())
                .comment(requestDto.getComment())
                .detail(requestDto.getDetail())
                .visitDate(requestDto.getVisitDate())
                .build();

        switch (requestDto.getReviewCategoryType()) {
            case PERFORMANCE:
                Hall hall = null;
                if (requestDto.getHallId() != null) {
                    hall = hallService.getHallEntityById(requestDto.getHallId());
                }
                PerformanceReviewDetail performanceDetail = PerformanceReviewDetail.builder()
                        .hall(hall)
                        .showName(requestDto.getShowName())
                        .artistName(requestDto.getArtistName())
                        .seatArea(requestDto.getSeatArea())
                        .seatRow(requestDto.getSeatRow())
                        .seatNumber(requestDto.getSeatNumber())
                        .seatDetail(requestDto.getSeatDetail())
                        .showDate(requestDto.getShowDate())
                        .round(requestDto.getRound())
                        .build();
                newReview.setPerformanceDetail(performanceDetail);
                break;
            case RESTAURANT:
                RestaurantReviewDetail restaurantDetail = RestaurantReviewDetail.builder()
                        .placeName(requestDto.getPlaceName())
                        .restaurantCategory(requestDto.getRestaurantCategory())
                        .restaurantAddress(requestDto.getRestaurantAddress())
                        .restaurantLatitude(requestDto.getRestaurantLatitude())
                        .restaurantLongitude(requestDto.getRestaurantLongitude())
                        .brandName(requestDto.getBrandName())
                        .build();
                newReview.setRestaurantDetail(restaurantDetail);
                break;
            case FACILITY:
                FacilityReviewDetail facilityDetail = FacilityReviewDetail.builder()
                        .facilityType(requestDto.getFacilityType())
                        .facilityTips(requestDto.getFacilityTips())
                        .facilityCategory(requestDto.getFacilityCategory())
                        .facilityAddress(requestDto.getFacilityAddress())
                        .facilityLatitude(requestDto.getFacilityLatitude())
                        .facilityLongitude(requestDto.getFacilityLongitude())
                        .convenienceRating(requestDto.getConvenienceRating())
                        .cleanlinessRating(requestDto.getCleanlinessRating())
                        .build();
                newReview.setFacilityDetail(facilityDetail);
                break;
            default:
                throw new IllegalArgumentException("Invalid review category type: " + requestDto.getReviewCategoryType());
        }

        // 이미지 처리
        if (requestDto.getImageUrls() != null && !requestDto.getImageUrls().isEmpty()) {
            for (String imageUrl : requestDto.getImageUrls()) {
                ReviewImage reviewImage = ReviewImage.builder()
                        .imageUrl(imageUrl)
                        .build();
                newReview.addImage(reviewImage);
            }
        }

        // 키워드 처리
        if (requestDto.getKeywords() != null && !requestDto.getKeywords().isEmpty()) {
            for (String keywordText : requestDto.getKeywords()) {
                newReview.addUserKeyword(UserKeyword.builder().keywordText(keywordText).build());
            }
        }

        Review savedReview = reviewRepository.save(newReview);

        ReviewResponseDto responseDto = new ReviewResponseDto(savedReview);
        if (savedReview.getReviewCategoryType() == ReviewCategory.PERFORMANCE && savedReview.getPerformanceDetail() != null && savedReview.getPerformanceDetail().getHall() != null) {
            HallResponseDto hallResponseDto = hallService.getHallById(savedReview.getPerformanceDetail().getHall().getId());
            responseDto.setHallInfo(hallResponseDto);
        }

        return responseDto;
    }

    @Transactional
    public ReviewResponseDto getReviewDetailById(Long reviewId) {
        Review review = reviewRepository.findByIdWithDetails(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with id: " + reviewId));

        review.setViewCount(review.getViewCount() + 1);

        ReviewResponseDto responseDto = new ReviewResponseDto(review);

        if (review.getReviewCategoryType() == ReviewCategory.PERFORMANCE && review.getPerformanceDetail() != null && review.getPerformanceDetail().getHall() != null) {
            HallResponseDto hallResponseDto = hallService.getHallById(review.getPerformanceDetail().getHall().getId());
            responseDto.setHallInfo(hallResponseDto);
        }

        return responseDto;
    }

    @Transactional
    public ReviewResponseDto updateReview(Long reviewId, Long userId, ReviewRequestDto requestDto) { // ⭐ DTO 이름 변경
        Review review = reviewRepository.findByIdWithDetails(reviewId)
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

        if (requestDto.getReviewCategoryType() == ReviewCategory.PERFORMANCE && review.getPerformanceDetail() != null) {
            PerformanceReviewDetail performanceDetail = review.getPerformanceDetail();
            Hall hall = null;
            if (requestDto.getHallId() != null) {
                hall = hallService.getHallEntityById(requestDto.getHallId());
            }
            performanceDetail.setHall(hall);
            performanceDetail.setShowName(requestDto.getShowName());
            performanceDetail.setArtistName(requestDto.getArtistName());
            performanceDetail.setSeatArea(requestDto.getSeatArea());
            performanceDetail.setSeatRow(requestDto.getSeatRow());
            performanceDetail.setSeatNumber(requestDto.getSeatNumber());
            performanceDetail.setSeatDetail(requestDto.getSeatDetail());
            performanceDetail.setShowDate(requestDto.getShowDate());
            performanceDetail.setRound(requestDto.getRound());

        } else if (requestDto.getReviewCategoryType() == ReviewCategory.RESTAURANT && review.getRestaurantDetail() != null) {
            RestaurantReviewDetail restaurantDetail = review.getRestaurantDetail();
            restaurantDetail.setPlaceName(requestDto.getPlaceName());
            restaurantDetail.setRestaurantCategory(requestDto.getRestaurantCategory());
            restaurantDetail.setRestaurantAddress(requestDto.getRestaurantAddress());
            restaurantDetail.setRestaurantLatitude(requestDto.getRestaurantLatitude());
            restaurantDetail.setRestaurantLongitude(requestDto.getRestaurantLongitude());
            restaurantDetail.setBrandName(requestDto.getBrandName());

        } else if (requestDto.getReviewCategoryType() == ReviewCategory.FACILITY && review.getFacilityDetail() != null) {
            FacilityReviewDetail facilityDetail = review.getFacilityDetail();
            facilityDetail.setFacilityType(requestDto.getFacilityType());
            facilityDetail.setFacilityTips(requestDto.getFacilityTips());
            facilityDetail.setFacilityCategory(requestDto.getFacilityCategory());
            facilityDetail.setFacilityAddress(requestDto.getFacilityAddress());
            facilityDetail.setFacilityLatitude(requestDto.getFacilityLatitude());
            facilityDetail.setFacilityLongitude(requestDto.getFacilityLongitude());
            facilityDetail.setConvenienceRating(requestDto.getConvenienceRating());
            facilityDetail.setCleanlinessRating(requestDto.getCleanlinessRating());
        } else {
            throw new IllegalArgumentException("Cannot change review type or invalid type for update.");
        }

        Review updatedReview = reviewRepository.save(review);

        ReviewResponseDto responseDto = new ReviewResponseDto(updatedReview);

        if (updatedReview.getReviewCategoryType() == ReviewCategory.PERFORMANCE && updatedReview.getPerformanceDetail() != null && updatedReview.getPerformanceDetail().getHall() != null) {
            HallResponseDto hallResponseDto = hallService.getHallById(updatedReview.getPerformanceDetail().getHall().getId());
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