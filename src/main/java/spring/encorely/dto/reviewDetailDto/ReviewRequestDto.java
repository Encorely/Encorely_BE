package spring.encorely.dto.reviewDetailDto;

import lombok.Data;
import lombok.NoArgsConstructor;
import spring.encorely.domain.reviewDetail.FacilityCategory;
import spring.encorely.domain.reviewDetail.RestaurantCategory;
import spring.encorely.domain.reviewDetail.ReviewCategory;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class ReviewRequestDto {

    private Long userId;
    private ReviewCategory reviewCategoryType;
    private Float rating;
    private String comment;
    private String detail;
    private LocalDate visitDate;
    private List<String> imageUrls;
    private List<String> keywords;

    // PerformanceReview
    private Long hallId;
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
    private FacilityCategory facilityCategory;
    private String facilityAddress;
    private String facilityLatitude;
    private String facilityLongitude;
    private Float convenienceRating;
    private Float cleanlinessRating;
}