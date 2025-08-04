package spring.encorely.service.reviewService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import spring.encorely.apiPayload.code.status.ErrorStatus;
import spring.encorely.apiPayload.exception.handler.ReviewHandler;
import spring.encorely.domain.enums.ReviewImageType;
import spring.encorely.domain.hall.Hall;
import spring.encorely.domain.review.PopularReviewCache;
import spring.encorely.domain.review.Review;
import spring.encorely.domain.review.ReviewImage;
import spring.encorely.domain.user.User;
import spring.encorely.dto.reviewDto.ReviewRequestDTO;
import spring.encorely.dto.reviewDto.ReviewResponseDTO;
import spring.encorely.exception.NotFoundException;
import spring.encorely.repository.reviewRepository.PopularReviewCacheRepository;
import spring.encorely.repository.reviewRepository.ReviewRepository;
import spring.encorely.repository.reviewRepository.ReviewStatsRepository;
import spring.encorely.service.hallService.HallService;
import spring.encorely.service.userService.UserService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final HallService hallService;
    private final ReviewImageService reviewImageService;
    private final UserKeywordsService userKeywordsService;
    private final RestaurantService restaurantService;
    private final FacilityService facilityService;
    private final ReviewStatsRepository reviewStatsRepository;
    private final PopularReviewCacheRepository popularReviewCacheRepository;

    public Review findById(Long id) {
        return reviewRepository.findById(id).orElseThrow(() -> new ReviewHandler(ErrorStatus.REVIEW_NOT_FOUND));
    }

    @Transactional
    public ReviewResponseDTO.CreateReview createReview(Long id, ReviewRequestDTO.CreateReview request) {
        User user = userService.findById(id);
        Hall hall = hallService.findById(request.getHallId());

        Review review = Review.builder()
                .user(user)
                .hall(hall)
                .date(request.getShowDate())
                .round(request.getRound())
                .showName(request.getShowName())
                .artistName(request.getArtistName())
                .seatArea(request.getSeatArea())
                .seatRow(request.getSeatRow())
                .seatNumber(request.getSeatNumber())
                .rating(request.getRating())
                .comment(request.getComment())
                .showDetail(request.getShowDetail())
                .seatDetail(request.getSeatDetail())
                .commentCount(0)
                .viewCount(0)
                .likeCount(0)
                .scrapCount(0)
                .build();

        reviewRepository.save(review);

        reviewImageService.saveReviewImages(review, request.getReviewImageInfos());
        userKeywordsService.saveHallKeywords(review, request.getConsAndProsList());
        restaurantService.saveRestaurant(review, request.getRestaurantInfos());
        facilityService.saveFacility(review, request.getFacilityInfos());

        user.setViewedShowCount(user.getViewedShowCount() + 1);

        return new ReviewResponseDTO.CreateReview(review.getId(), review.getCreatedAt());
    }

    @Transactional
    public void incrementReviewViewCount(Long reviewId) {
        Review review = findById(reviewId);
        review.setViewCount(review.getViewCount() + 1);
    }

    @Transactional
    public ReviewResponseDTO.GetReview getReviewDetailById(Long reviewId) {
        incrementReviewViewCount(reviewId);
        Review review = findById(reviewId);

        return ReviewResponseDTO.GetReview.builder()
                .reviewId(review.getId())
                .nickname(review.getUser().getNickname())
                .date(review.getDate())
                .round(review.getRound())
                .showName(review.getShowName())
                .showImages(reviewImageService.getImages(ReviewImageType.SHOW, review))
                .comment(review.getComment())
                .showDetail(review.getShowDetail())
                .viewImages(reviewImageService.getImages(ReviewImageType.VIEW, review))
                .hallId(review.getHall().getId())
                .hallName(review.getHall().getName())
                .seatArea(review.getSeatArea())
                .seatRow(review.getSeatRow())
                .seatNumber(review.getSeatNumber())
                .rating(review.getRating())
                .seatDetail(review.getSeatDetail())
                .seatKeywords(userKeywordsService.getKeywords(review, null))
                .restaurants(restaurantService.getRestaurants(review))
                .facilities(facilityService.getFacilities(review))
                .likeCount(review.getLikeCount())
                .commentCount(review.getCommentCount())
                .build();
    }

    @Transactional
    public void updateReview(Long reviewId, ReviewRequestDTO.UpdateReview request) {
        Review review = findById(reviewId);

        if (request.getShowDate() != null) { review.setDate(request.getShowDate()); }

        if (request.getRound() != null) { review.setRound(request.getRound()); }

        if (request.getShowName() != null) { review.setShowName(request.getShowName()); }

        if (request.getArtistName() != null) { review.setArtistName(request.getArtistName()); }

        if (request.getHallId() != null) { review.setHall(hallService.findById(request.getHallId())); }

        if (request.getSeatArea() != null) { review.setSeatArea(request.getSeatArea()); }

        if (request.getSeatRow() != null) { review.setSeatRow(request.getSeatRow()); }

        if (request.getSeatNumber() != null) { review.setSeatNumber(request.getSeatNumber()); }

        if (request.getRating() != null) { review.setRating(request.getRating()); }

        if (request.getConsAndProsList() != null) {
            review.getKeywordList().clear();
            userKeywordsService.saveHallKeywords(review, request.getConsAndProsList());
        }

        if (request.getSeatDetail() != null) { review.setSeatDetail(request.getSeatDetail()); }

        if (request.getComment() != null) { review.setComment(request.getComment()); }

        if (request.getShowDetail() != null) { review.setShowDetail(request.getShowDetail()); }

        if (request.getReviewImageInfos() != null) { reviewImageService.saveReviewImages(review, request.getReviewImageInfos()); }

        if (request.getRestaurantInfos() != null) { restaurantService.updateRestaurants(request.getRestaurantInfos()); }

        if (request.getFacilityInfos() != null) { facilityService.updateFacilities(request.getFacilityInfos()); }

    }

    @Transactional
    public void deleteReview(Long reviewId, Long userId) {
        Review review = findById(reviewId);
        User user = userService.findById(userId);
        reviewImageService.deleteAllImages(review);
        user.setViewedShowCount(user.getViewedShowCount() - 1);
        reviewRepository.delete(review);
    }

    public List<ReviewResponseDTO.PopularReviewInfo> getPopularReviews() {
        List<PopularReviewCache> cached = popularReviewCacheRepository.findAll();

        return cached.stream().map(entry -> {
            Review review = entry.getReview();
            String showImageUrl = review.getReviewImageList().stream()
                    .filter(img -> img.getType() == ReviewImageType.SHOW)
                    .map(ReviewImage::getImageUrl)
                    .findFirst()
                    .orElse(null);

            return ReviewResponseDTO.PopularReviewInfo.builder()
                    .reviewId(review.getId())
                    .reviewImageUrl(showImageUrl)
                    .userProfileImageUrl(review.getUser().getImageUrl())
                    .nickname(review.getUser().getNickname())
                    .build();
        }).toList();
    }

}
