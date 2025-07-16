package spring.encorely.service.reviewService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.encorely.apiPayload.code.status.ErrorStatus;
import spring.encorely.apiPayload.exception.handler.ReviewHandler;
import spring.encorely.domain.enums.ReviewImageCategory;
import spring.encorely.domain.review.Facility;
import spring.encorely.domain.review.Restaurant;
import spring.encorely.domain.review.Review;
import spring.encorely.domain.review.ReviewImage;
import spring.encorely.dto.reviewDto.ReviewRequestDTO;
import spring.encorely.repository.reviewRepository.ReviewImageRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewImageService {

    private final ReviewImageRepository reviewImageRepository;

    public ReviewImage findById(Long id) {
        return reviewImageRepository.findById(id).orElseThrow(() -> new ReviewHandler(ErrorStatus.REVIEW_IMAGE_NOT_FOUND));
    }

    public ReviewImage findByUrl(String url) {
        return reviewImageRepository.findByImageUrl(url).orElseThrow(() -> new ReviewHandler(ErrorStatus.REVIEW_IMAGE_NOT_FOUND));
    }

    @Transactional
    public void saveReviewImages(Review review, List<ReviewRequestDTO.ReviewImageInfo> infoList) {
        for (ReviewRequestDTO.ReviewImageInfo image : infoList) {
            ReviewImage reviewImage = findByUrl(image.getImageUrl());

            reviewImage.markAsUsed(
                    ReviewImageCategory.REVIEW,
                    image.getImageType(),
                    review,
                    null,
                    null
            );
        }
    }

    @Transactional
    public void saveRestaurantImages(Restaurant restaurant, List<String> urls) {
        for (String url : urls) {
            ReviewImage reviewImage = findByUrl(url);

            reviewImage.markAsUsed(
                    ReviewImageCategory.RESTAURANT,
                    null,
                    null,
                    restaurant,
                    null
            );
        }
    }

    @Transactional
    public void saveFacilityImages(Facility facility, List<String> urls) {
        for (String url : urls) {
            ReviewImage reviewImage = findByUrl(url);

            reviewImage.markAsUsed(
                    ReviewImageCategory.FACILITY,
                    null,
                    null,
                    null,
                    facility
            );
        }
    }

}
