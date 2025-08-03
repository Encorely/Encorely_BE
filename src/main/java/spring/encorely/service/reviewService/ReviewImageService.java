package spring.encorely.service.reviewService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.encorely.apiPayload.code.status.ErrorStatus;
import spring.encorely.apiPayload.exception.handler.ReviewHandler;
import spring.encorely.apiPayload.exception.handler.UserHandler;
import spring.encorely.domain.enums.ReviewImageCategory;
import spring.encorely.domain.enums.ReviewImageType;
import spring.encorely.domain.review.Facility;
import spring.encorely.domain.review.Restaurant;
import spring.encorely.domain.review.Review;
import spring.encorely.domain.review.ReviewImage;
import spring.encorely.domain.user.User;
import spring.encorely.dto.reviewDto.ReviewRequestDTO;
import spring.encorely.dto.reviewDto.ReviewResponseDTO;
import spring.encorely.repository.reviewRepository.ReviewImageRepository;
import spring.encorely.service.s3Service.S3Service;
import spring.encorely.service.userService.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewImageService {

    private final ReviewImageRepository reviewImageRepository;
    private final S3Service s3Service;
    private final UserService userService;

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
    public void saveRestaurantImage(Restaurant restaurant, String url) {
        ReviewImage reviewImage = findByUrl(url);

        reviewImage.markAsUsed(
                ReviewImageCategory.RESTAURANT,
                null,
                null,
                restaurant,
                null
        );
    }

    @Transactional
    public void saveFacilityImage(Facility facility, String url) {
        ReviewImage reviewImage = findByUrl(url);

        reviewImage.markAsUsed(
                ReviewImageCategory.FACILITY,
                null,
                null,
                null,
                facility
        );
    }

    @Transactional
    public ReviewResponseDTO.CreateReviewImage createReviewImage(String key) {
        String imageUrl = s3Service.getPublicUrl(key);

        ReviewImage reviewImage = ReviewImage.builder()
                .imageUrl(imageUrl)
                .category(null)
                .type(null)
                .review(null)
                .used(false)
                .build();

        reviewImageRepository.save(reviewImage);

        return new ReviewResponseDTO.CreateReviewImage(reviewImage.getId(), reviewImage.getImageUrl(), reviewImage.getCreatedAt());
    }

    @Transactional
    public void deleteReviewImage(Long imageId) {
        ReviewImage reviewImage = findById(imageId);

        s3Service.delete(reviewImage.getImageUrl());

        reviewImageRepository.delete(reviewImage);
    }

    @Transactional
    public void deleteAllImages(Review review) {
        List<ReviewImage> reviewImages = review.getReviewImageList();
        List<Restaurant> restaurants = review.getRestaurantList();
        List<Facility> facilities = review.getFacilityList();

        for (Restaurant restaurant : restaurants) {
            reviewImages.add(restaurant.getReviewImage());
        }

        for (Facility facility : facilities) {
            reviewImages.add(facility.getReviewImage());
        }

        s3Service.deleteAllImages(reviewImages);
    }

    public List<ReviewResponseDTO.Image> getImages(ReviewImageType type, Review review) {
        List<ReviewResponseDTO.Image> dtos = new ArrayList<>();
        List<ReviewImage> reviewImages = reviewImageRepository.findAllByReviewAndType(review, type);

        for (ReviewImage reviewImage : reviewImages) {
            ReviewResponseDTO.Image dto = ReviewResponseDTO.Image.builder()
                    .imageId(reviewImage.getId())
                    .imageUrl(reviewImage.getImageUrl())
                    .build();

            dtos.add(dto);
        }

        return dtos;
    }

    public ReviewResponseDTO.Image getImage(Restaurant restaurant, Facility facility) {
        ReviewImage reviewImage = null;
        if (restaurant != null) {
            reviewImage = reviewImageRepository.findByRestaurant(restaurant);
        } else reviewImage = reviewImageRepository.findByFacility(facility);

        return ReviewResponseDTO.Image.builder()
                .imageId(reviewImage.getId())
                .imageUrl(reviewImage.getImageUrl())
                .build();
    }

}
