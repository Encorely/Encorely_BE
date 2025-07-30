package spring.encorely.dto.reviewDetailDto;

import lombok.Getter;
import lombok.Setter;
import spring.encorely.domain.reviewDetail.ReviewCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ReviewRequestDto {

    @NotNull(message = "사용자 ID는 필수입니다.")
    private Long userId;

    @NotNull(message = "리뷰 카테고리는 필수입니다.")
    private ReviewCategory reviewCategoryType;

    @NotNull(message = "평점은 필수입니다.")
    @Min(value = 0, message = "평점은 0 이상이어야 합니다.")
    @Max(value = 5, message = "평점은 5 이하여야 합니다.")
    private Float rating;

    @NotBlank(message = "한줄평은 필수입니다.")
    private String comment;

    private String detail;

    @NotNull(message = "방문 날짜는 필수입니다.")
    private LocalDate visitDate;

    private List<String> imageUrls;
    private Set<String> keywords;

    // Performance Review Detail
    private Long hallId;
    private String performanceShowName;
    private String performanceArtistName;
    private String performanceSeatArea;
    private String performanceSeatRow;
    private String performanceSeatNumber;
    private String performanceSeatDetail;
    private LocalDate performanceShowDate;
    private Integer performanceRound;

    // Restaurant Review Detail
    private String restaurantPlaceName;
    private String restaurantCategory;
    private String restaurantAddress;
    private Double restaurantLatitude;
    private Double restaurantLongitude;
    private String restaurantBrandName;

    // Facility Review Detail
    private String facilityType;
    private String facilityTips;
    private String facilityCategory;
    private String facilityAddress;
    private Double facilityLatitude;
    private Double facilityLongitude;
    private Float facilityConvenienceRating;
    private Float facilityCleanlinessRating;
}