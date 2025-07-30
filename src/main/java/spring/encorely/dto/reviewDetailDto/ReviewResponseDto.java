package spring.encorely.dto.reviewDetailDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spring.encorely.domain.reviewDetail.Review;
import spring.encorely.domain.reviewDetail.ReviewCategory;
import spring.encorely.dto.hallDto.HallResponseDto;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ReviewResponseDto {
    private Long id;
    private String userNickname;
    private ReviewCategory reviewCategoryType;
    private Float rating;
    private String comment;
    private String detail;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private Integer scrapCount;
    private LocalDateTime createdAt;
    private LocalDate visitDate;

    // Performance Review Detail
    private HallResponseDto hallInfo;
    private String performanceShowName;
    private String performanceArtistName;
    private String performanceSeatArea;
    private String performanceSeatRow;
    private String performanceSeatNumber;
    private String performanceSeatDetail;
    private LocalDate performanceShowDate;
    private Integer performanceRound;

    //Restaurant Review Detail
    private String restaurantPlaceName;
    private String restaurantCategory;
    private String restaurantAddress;
    private Double restaurantLatitude;
    private Double restaurantLongitude;
    private String restaurantBrandName;

    //Facility Review Detail
    private String facilityType;
    private String facilityTips;
    private String facilityCategory;
    private String facilityAddress;
    private Double facilityLatitude;
    private Double facilityLongitude;
    private Float facilityConvenienceRating;
    private Float facilityCleanlinessRating;

    private List<String> imageUrls;
    private Set<String> keywords;

    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.userNickname = review.getUser() != null ? review.getUser().getNickname() : null;
        this.reviewCategoryType = review.getReviewCategoryType();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.detail = review.getDetail();
        this.viewCount = review.getViewCount();
        this.likeCount = review.getLikeCount();
        this.commentCount = review.getCommentCount();
        this.scrapCount = review.getScrapCount();
        this.createdAt = review.getCreatedAt();
        this.visitDate = review.getVisitDate();


        // Performance
        this.performanceShowName = review.getPerformanceShowName();
        this.performanceArtistName = review.getPerformanceArtistName();
        this.performanceSeatArea = review.getPerformanceSeatArea();
        this.performanceSeatRow = review.getPerformanceSeatRow();
        this.performanceSeatNumber = review.getPerformanceSeatNumber();
        this.performanceSeatDetail = review.getPerformanceSeatDetail();
        this.performanceShowDate = review.getPerformanceShowDate();
        this.performanceRound = review.getPerformanceRound();

        // Restaurant
        this.restaurantPlaceName = review.getRestaurantPlaceName();
        this.restaurantCategory = review.getRestaurantCategory();
        this.restaurantAddress = review.getRestaurantAddress();
        this.restaurantLatitude = review.getRestaurantLatitude();
        this.restaurantLongitude = review.getRestaurantLongitude();
        this.restaurantBrandName = review.getRestaurantBrandName();

        // Facility
        this.facilityType = review.getFacilityType();
        this.facilityTips = review.getFacilityTips();
        this.facilityCategory = review.getFacilityCategory();
        this.facilityAddress = review.getFacilityAddress();
        this.facilityLatitude = review.getFacilityLatitude();
        this.facilityLongitude = review.getFacilityLongitude();
        this.facilityConvenienceRating = review.getFacilityConvenienceRating();
        this.facilityCleanlinessRating = review.getFacilityCleanlinessRating();

        this.imageUrls = review.getImages().stream()
                .map(image -> image.getImageUrl())
                .collect(Collectors.toList());
        this.keywords = review.getUserKeywords().stream()
                .map(keyword -> keyword.getKeywordText())
                .collect(Collectors.toSet());
    }
}