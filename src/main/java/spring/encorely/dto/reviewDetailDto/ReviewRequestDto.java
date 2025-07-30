package spring.encorely.dto.reviewDetailDto;

import lombok.*;
import spring.encorely.domain.reviewDetail.ReviewCategory;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequestDto {
    private Long userId;
    private ReviewCategory reviewCategoryType;
    private Float rating;
    private String comment;
    private String detail;
    private LocalDate visitDate;

    // Performance Detail
    private Long hallId;
    private String performanceShowName;
    private String performanceArtistName;
    private String performanceSeatArea;
    private String performanceSeatRow;
    private String performanceSeatNumber;
    private String performanceSeatDetail;
    private LocalDate performanceShowDate;
    private Integer performanceRound;

    // Restaurant Detail
    private String restaurantPlaceName;
    private String restaurantCategory;
    private String restaurantAddress;
    private Float restaurantLatitude;
    private Float restaurantLongitude;
    private String restaurantBrandName;

    // Facility Detail
    private String facilityType;
    private String facilityTips;
    private String facilityCategory;
    private String facilityAddress;
    private Double facilityLatitude;
    private Double facilityLongitude;
    private Float facilityConvenienceRating;
    private Float facilityCleanlinessRating;

    private List<String> imageUrls;

    //keyword
    private List<Long> performanceSeatKeywordIds;
    private List<Long> restaurantKeywordIds;
    private List<Long> generalKeywordIds;
}