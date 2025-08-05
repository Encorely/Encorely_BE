package spring.encorely.dto.reviewDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewReview {
        private Long reviewId;
        private String showName;
        private String artistName;
        private String seatArea;
        private String seatRow;
        private String seatNumber;
        private Float rating;
        private int scrapCount;
        private int commentCount;
        private int viewCount;
        private String thumbnailImageUrl;
    }

}
