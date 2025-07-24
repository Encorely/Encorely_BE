package spring.encorely.service.reviewService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.encorely.domain.review.Restaurant;
import spring.encorely.domain.review.Review;
import spring.encorely.dto.reviewDto.ReviewRequestDTO;
import spring.encorely.repository.reviewRepository.RestaurantRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserKeywordsService userKeywordsService;
    private final ReviewImageService reviewImageService;

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

}
