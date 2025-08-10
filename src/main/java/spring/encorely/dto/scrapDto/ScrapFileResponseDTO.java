package spring.encorely.dto.scrapDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ScrapFileResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class addReviewToFile{
        Long reviewScrapId;
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class addFile{
        Long fileId;
        String name;
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class updateFileName{
        Long fileId;
        String name;
        LocalDateTime updatedAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetFile {
        Integer numOfFiles;
        List<FileInfo> files;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FileInfo {
        Long fileId;
        String imageUrl;
        String name;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetScrapReview {
        Integer numOfReviews;
        List<ReviewInfo> reviews;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewInfo {
        Long reviewId;
        Long userId;
        String userProfileImageUrl;
        String nickname;
        String seatArea;
        String seatRow;
        String seatNumber;
        Integer scrapCount;
        Float rating;
        List<String> imageUrls;
        String showDetail;
        String representativeKeyword;
        Integer numOfKeywords;
    }

}
