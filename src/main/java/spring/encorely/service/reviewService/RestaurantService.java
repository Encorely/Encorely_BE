package spring.encorely.service.reviewService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.encorely.apiPayload.code.status.ErrorStatus;
import spring.encorely.apiPayload.exception.handler.RestaurantHandler;
import spring.encorely.domain.review.Restaurant;
import spring.encorely.domain.review.Review;
import spring.encorely.dto.reviewDto.ReviewRequestDTO;
import spring.encorely.dto.reviewDto.ReviewResponseDTO;
import spring.encorely.repository.reviewRepository.RestaurantRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserKeywordsService userKeywordsService;
    private final ReviewImageService reviewImageService;

    public Restaurant findById(Long id) {
        return restaurantRepository.findById(id).orElseThrow(() -> new RestaurantHandler(ErrorStatus.RESTAURANT_NOT_FOUND));
    }

    @Transactional
    public void saveRestaurant(Review review, List<ReviewRequestDTO.RestaurantInfo> infos) {
        for (ReviewRequestDTO.RestaurantInfo info : infos) {
            Restaurant restaurant = Restaurant.builder()
                    .review(review)
                    .name(info.getName())
                    .address(info.getAddress())
                    .latitude(info.getLatitude())
                    .longitude(info.getLongitude())
                    .restaurantDetail(info.getRestaurantDetail())
                    .restaurantType(info.getRestaurantType())
                    .build();

            restaurantRepository.save(restaurant);
            userKeywordsService.saveRestaurantKeywords(restaurant, info.getRestaurantProsList());
            reviewImageService.saveRestaurantImage(restaurant, info.getImageUrl());
        }
    }

    public List<ReviewResponseDTO.Restaurant> getRestaurants(Review review) {
        List<ReviewResponseDTO.Restaurant> dtos = new ArrayList<>();
        List<Restaurant> restaurants = restaurantRepository.findAllByReview(review);

        for (Restaurant restaurant : restaurants) {
            ReviewResponseDTO.Restaurant dto = ReviewResponseDTO.Restaurant.builder()
                    .restaurantId(restaurant.getId())
                    .restaurantType(restaurant.getRestaurantType())
                    .image(reviewImageService.getImage(restaurant, null))
                    .restaurantName(restaurant.getName())
                    .address(restaurant.getAddress())
                    .latitude(restaurant.getLatitude())
                    .longitude(restaurant.getLongitude())
                    .restaurantDetail(restaurant.getRestaurantDetail())
                    .restaurantKeywords(userKeywordsService.getKeywords(null, restaurant))
                    .build();

            dtos.add(dto);
        }

        return dtos;
    }

    @Transactional
    public void updateRestaurants(List<ReviewRequestDTO.RestaurantUpdateInfo> infos) {
        for (ReviewRequestDTO.RestaurantUpdateInfo info : infos) {
            Restaurant restaurant = findById(info.getRestaurantId());

            if (info.getRestaurantType() != null) { restaurant.setRestaurantType(info.getRestaurantType()); }

            if (info.getName() != null) { restaurant.setName(info.getName()); }

            if (info.getAddress() != null) { restaurant.setAddress(info.getAddress()); }

            if (info.getLatitude() != null) { restaurant.setLatitude(info.getLatitude()); }

            if (info.getLongitude() != null) { restaurant.setLongitude(info.getLongitude()); }

            if (info.getRestaurantDetail() != null) { restaurant.setRestaurantDetail(info.getRestaurantDetail()); }

            if (info.getImageUrl() != null) { reviewImageService.saveRestaurantImage(restaurant, info.getImageUrl()); }

            if (info.getRestaurantProsList() != null) {
                restaurant.getKeywordList().clear();
                userKeywordsService.saveRestaurantKeywords(restaurant, info.getRestaurantProsList());
            }

        }
    }

}
