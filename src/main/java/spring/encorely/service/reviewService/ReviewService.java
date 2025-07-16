package spring.encorely.service.reviewService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.encorely.apiPayload.code.status.ErrorStatus;
import spring.encorely.apiPayload.exception.handler.ReviewHandler;
import spring.encorely.domain.hall.Hall;
import spring.encorely.domain.review.Review;
import spring.encorely.domain.user.User;
import spring.encorely.dto.reviewDto.ReviewRequestDTO;
import spring.encorely.dto.reviewDto.ReviewResponseDTO;
import spring.encorely.repository.reviewRepository.ReviewRepository;
import spring.encorely.service.hallService.HallService;
import spring.encorely.service.userService.UserService;

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
                .build();

        reviewRepository.save(review);

        reviewImageService.saveReviewImages(review, request.getReviewImageInfos());
        userKeywordsService.saveHallKeywords(review, request.getConsAndProsList());
        restaurantService.saveRestaurant(review, request.getRestaurantInfos());
        facilityService.saveFacility(review, request.getFacilityInfos());

        return new ReviewResponseDTO.CreateReview(review.getId(), review.getCreatedAt());
    }

}
