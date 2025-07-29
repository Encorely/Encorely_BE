package spring.encorely.dto.reviewDetailDto;

import lombok.Data;
import lombok.NoArgsConstructor;
import spring.encorely.domain.reviewDetail.*;
import spring.encorely.dto.commentDto.CommentResponseDto;
import spring.encorely.dto.hallDto.HallResponseDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ReviewResponseDto {

    private Long id;
    private ReviewCategory reviewCategoryType;
    private Long userId;
    private String userNickname;
    private Float rating;
    private String comment;
    private String detail;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private Integer scrapCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDate visitDate;
    private List<String> imageUrls;
    private List<CommentResponseDto> comments;
    private List<String> keywords;

    // PerformanceReview
    private Long hallId;
    private String hallName;
    private String showName;
    private String artistName;
    private String seatArea;
    private String seatRow;
    private String seatNumber;
    private String seatDetail;
    private LocalDate showDate;
    private Integer round;

    // RestaurantReview
    private String placeName;
    private RestaurantCategory restaurantCategory;
    private String restaurantAddress;
    private String restaurantLatitude;
    private String restaurantLongitude;
    private String brandName;

    // FacilityReview
    private String facilityType;
    private String facilityTips;
    private String facilityCategory;
    private String facilityAddress;
    private String facilityLatitude;
    private String facilityLongitude;
    private Float convenienceRating;
    private Float cleanlinessRating;

    private HallResponseDto hallInfo;

    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.userId = review.getUser().getId();
        this.userNickname = review.getUser().getNickname();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.detail = review.getDetail();
        this.viewCount = review.getViewCount();
        this.likeCount = review.getLikeCount();
        this.commentCount = review.getCommentCount();
        this.scrapCount = review.getScrapCount();
        this.createdAt = review.getCreatedAt();
        this.updatedAt = review.getUpdatedAt();
        this.visitDate = review.getVisitDate();
        this.reviewCategoryType = review.getReviewCategoryType();

        this.imageUrls = review.getImages().stream()
                .map(ReviewImage::getImageUrl)
                .collect(Collectors.toList());

        this.comments = review.getComments().stream()
                .filter(c -> c.getParent() == null)
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());

        this.keywords = review.getUserKeywords().stream()
                .map(UserKeyword::getKeywordText)
                .collect(Collectors.toList());

        if (review.getReviewCategoryType() == ReviewCategory.PERFORMANCE && review.getPerformanceDetail() != null) {
            PerformanceReviewDetail performanceDetail = review.getPerformanceDetail();
            this.hallId = performanceDetail.getHall() != null ? performanceDetail.getHall().getId() : null;
            this.hallName = performanceDetail.getHall() != null ? performanceDetail.getHall().getName() : null;
            this.showName = performanceDetail.getShowName();
            this.artistName = performanceDetail.getArtistName();
            this.seatArea = performanceDetail.getSeatArea();
            this.seatRow = performanceDetail.getSeatRow();
            this.seatNumber = performanceDetail.getSeatNumber();
            this.seatDetail = performanceDetail.getSeatDetail();
            this.showDate = performanceDetail.getShowDate();
            this.round = performanceDetail.getRound();

        } else if (review.getReviewCategoryType() == ReviewCategory.RESTAURANT && review.getRestaurantDetail() != null) {
            RestaurantReviewDetail restaurantDetail = review.getRestaurantDetail();
            this.placeName = restaurantDetail.getPlaceName();
            this.restaurantCategory = restaurantDetail.getRestaurantCategory();
            this.restaurantAddress = restaurantDetail.getRestaurantAddress();
            this.restaurantLatitude = restaurantDetail.getRestaurantLatitude();
            this.restaurantLongitude = restaurantDetail.getRestaurantLongitude();
            this.brandName = restaurantDetail.getBrandName();

        } else if (review.getReviewCategoryType() == ReviewCategory.FACILITY && review.getFacilityDetail() != null) {
            FacilityReviewDetail facilityDetail = review.getFacilityDetail();
            this.facilityType = facilityDetail.getFacilityType();
            this.facilityTips = facilityDetail.getFacilityTips();
            this.facilityCategory = (facilityDetail.getFacilityCategory() != null) ? facilityDetail.getFacilityCategory().name() : null;
            this.facilityAddress = facilityDetail.getFacilityAddress();
            this.facilityLatitude = facilityDetail.getFacilityLatitude();
            this.facilityLongitude = facilityDetail.getFacilityLongitude();
            this.convenienceRating = facilityDetail.getConvenienceRating();
            this.cleanlinessRating = facilityDetail.getCleanlinessRating();
        }
    }
}