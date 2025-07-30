package spring.encorely.dto.reviewDetailDto;

import lombok.*;
import spring.encorely.domain.reviewDetail.KeywordCategory;
import spring.encorely.domain.reviewDetail.Review;
import spring.encorely.domain.reviewDetail.ReviewImage;
import spring.encorely.domain.reviewDetail.UserKeyword;
import spring.encorely.dto.hallDto.HallResponseDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {

    private Long id;
    private Long userId;
    private String userNickname;

    private Float rating;

    private String comment;
    private String detail;
    private LocalDate visitDate;
    private int viewCount;
    private int likeCount;
    private int scrapCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 이미지
    private List<String> imageUrls;

    // 홀 정보
    private HallResponseDto hallInfo;

    // 공연 관련
    private String performanceShowName;
    private String performanceArtistName;
    private String performanceSeatArea;
    private String performanceSeatRow;
    private String performanceSeatNumber;
    private String performanceSeatDetail;
    private LocalDate performanceShowDate;
    private Integer performanceRound;
    private List<String> performanceSeatKeywords; // 좌석 키워드 리스트

    // 식당 관련
    private String restaurantPlaceName;
    private String restaurantCategory;
    private String restaurantAddress;
    private Float restaurantLatitude;
    private Float restaurantLongitude;
    private String restaurantBrandName;
    private List<String> restaurantKeywords; // 맛집 키워드 리스트

    // 편의시설 관련
    private String facilityType;
    private String facilityTips;
    private String facilityCategory;
    private String facilityAddress;
    private Double facilityLatitude;
    private Double facilityLongitude;
    private Float facilityConvenienceRating;
    private Float facilityCleanlinessRating;
    private List<String> generalKeywords;

    // 생성자
    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.userId = review.getUser().getId();
        this.userNickname = review.getUser().getNickname();

        this.rating = review.getRating();

        this.comment = review.getComment();
        this.detail = review.getDetail();
        this.visitDate = review.getVisitDate();
        this.viewCount = review.getViewCount();
        this.likeCount = review.getLikeCount();
        this.scrapCount = review.getScrapCount();
        this.createdAt = review.getCreatedAt();
        this.updatedAt = review.getUpdatedAt();

        this.imageUrls = review.getImages().stream()
                .map(ReviewImage::getImageUrl)
                .collect(Collectors.toList());

        // 공연 관련 정보
        this.performanceShowName = review.getPerformanceShowName();
        this.performanceArtistName = review.getPerformanceArtistName();
        this.performanceSeatArea = review.getPerformanceSeatArea();
        this.performanceSeatRow = review.getPerformanceSeatRow();
        this.performanceSeatNumber = review.getPerformanceSeatNumber();
        this.performanceSeatDetail = review.getPerformanceSeatDetail();
        this.performanceShowDate = review.getPerformanceShowDate();
        this.performanceRound = review.getPerformanceRound();

        // 좌석 키워드 필터링 및 매핑
        this.performanceSeatKeywords = review.getUserKeywords().stream()
                .filter(uk -> uk.getKeyword().getCategory() == KeywordCategory.PERFORMANCE_SEAT)
                .map(uk -> uk.getKeyword().getContent())
                .collect(Collectors.toList());

        // 식당 관련 정보
        this.restaurantPlaceName = review.getRestaurantPlaceName();
        this.restaurantCategory = review.getRestaurantCategory();
        this.restaurantAddress = review.getRestaurantAddress();
        this.restaurantLatitude = review.getRestaurantLatitude(); // 이젠 Float 타입으로 매핑될 것입니다.
        this.restaurantLongitude = review.getRestaurantLongitude(); // 이젠 Float 타입으로 매핑될 것입니다.
        this.restaurantBrandName = review.getRestaurantBrandName();

        this.restaurantKeywords = review.getUserKeywords().stream()
                .filter(uk -> uk.getKeyword().getCategory() == KeywordCategory.RESTAURANT)
                .map(uk -> uk.getKeyword().getContent())
                .collect(Collectors.toList());

        // 편의시설 관련 정보
        this.facilityType = review.getFacilityType();
        this.facilityTips = review.getFacilityTips();
        this.facilityCategory = review.getFacilityCategory();
        this.facilityAddress = review.getFacilityAddress();
        this.facilityLatitude = review.getFacilityLatitude();
        this.facilityLongitude = review.getFacilityLongitude();
        this.facilityConvenienceRating = review.getFacilityConvenienceRating();
        this.facilityCleanlinessRating = review.getFacilityCleanlinessRating();

    }
}