package spring.encorely.dto.reviewDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.encorely.domain.enums.FacilityType;
import spring.encorely.domain.enums.RestaurantType;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class ReviewResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateReview {
        Long reviewId;
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateReviewImage {
        Long imageId;
        String imageUrl;
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetReview {
        Long reviewId;
        String nickname;
        Date date;
        Integer round;
        String showName;
        List<Image> showImages;
        String comment;
        String showDetail;
        List<Image> viewImages;
        Long hallId;
        String hallName;
        String seatArea;
        String seatRow;
        String seatNumber;
        Float rating;
        String seatDetail;
        List<Keyword> seatKeywords;
        List<Restaurant> restaurants;
        List<Facility> facilities;
        Integer likeCount;
        Integer commentCount;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Image {
        Long imageId;
        String imageUrl;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Keyword {
        Long keywordId;
        String content;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Restaurant {
        Long restaurantId;
        RestaurantType restaurantType;
        Image image;
        String restaurantName;
        String address;
        String latitude;
        String longitude;
        String restaurantDetail;
        List<Keyword> restaurantKeywords;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Facility {
        Long facilityId;
        FacilityType facilityType;
        Image image;
        String facilityName;
        String address;
        String latitude;
        String longitude;
        String tips;
    }

}
