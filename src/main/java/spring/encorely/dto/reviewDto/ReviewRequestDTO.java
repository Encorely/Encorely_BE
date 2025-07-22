package spring.encorely.dto.reviewDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import spring.encorely.domain.enums.FacilityType;
import spring.encorely.domain.enums.RestaurantType;
import spring.encorely.domain.enums.ReviewImageType;

import java.util.Date;
import java.util.List;

public class ReviewRequestDTO {

    @Getter
    @Setter
    public static class CreateReview {

        @Schema(description = "공연 날짜", example = "2025-07-17")
        @NotNull
        Date showDate;
        @Schema(description = "회차", example = "1, 2, 3")
        @NotNull
        Integer round;
        @Schema(description = "공연명")
        @NotBlank
        String showName;
        @Schema(description = "아티스트명")
        @NotBlank
        String artistName;
        @Schema(description = "공연장 아이디", example = "1, 2, 3")
        @NotNull
        Long hallId;
        @Schema(description = "좌석 구역", example = "A, 1")
        @NotBlank
        String seatArea;
        @Schema(description = "좌석 열", example = "A, 1")
        @NotBlank
        String seatRow;
        @Schema(description = "좌석 번호", example = "A, 1")
        @NotBlank
        String seatNumber;
        @Schema(description = "별점", example = "2.5, 3")
        @NotNull
        Float rating;
        @Schema(description = "공연장 좋은 점 + 별로인 점 아이디 리스트", example = "1, 2, 3")
        List<Long> consAndProsList;
        @Schema(description = "좌석 상세 후기")
        String seatDetail;
        @Schema(description = "한줄평")
        @NotBlank
        String comment;
        @Schema(description = "공연 자세한 후기")
        String showDetail;
        @Schema(description = "시야/공연 사진 정보")
        List<ReviewImageInfo> reviewImageInfos;
        @Schema(description = "맛집 정보")
        List<RestaurantInfo> restaurantInfos;
        @Schema(description = "편의시설 정보")
        List<FacilityInfo> facilityInfos;

    }

    @Getter
    @Setter
    public static class ReviewImageInfo {

        @Schema(description = "이미지 등록 후 반환된 URL", example = "https://s3...")
        @NotBlank
        String imageUrl;
        @Schema(description = "사진 종류", example = "VIEW, SHOW")
        @NotNull
        ReviewImageType imageType;

    }

    @Getter
    @Setter
    public static class RestaurantInfo {

        @Schema(description = "맛집 종류", example = "MEAL, CAFE, DRINK")
        @NotNull
        RestaurantType restaurantType;
        @Schema(description = "맛집 이름")
        @NotBlank
        String name;
        @Schema(description = "맛집 주소")
        @NotBlank
        String address;
        @Schema(description = "맛집 위도")
        String latitude;
        @Schema(description = "맛집 경도")
        String longitude;
        @Schema(description = "맛집 자세한 후기")
        String restaurantDetail;
        @Schema(description = "이미지 등록 후 반환된 URL", example = "https://s3...")
        String imageUrl;
        @Schema(description = "맛집 키워드 아이디 리스트", example = "1, 2, 3")
        List<Long> restaurantProsList;

    }

    @Getter
    @Setter
    public static class FacilityInfo {

        @Schema(description = "편의시설 종류", example = "CONVENIENCE_STORE, TOILET, PARKING_LOT, BENCH, ATM, ETC")
        @NotNull
        FacilityType facilityType;
        @Schema(description = "맛집 이름(없으면 생략 가능)")
        String name;
        @Schema(description = "편의시설 주소")
        @NotBlank
        String address;
        @Schema(description = "편의시설 위도")
        String latitude;
        @Schema(description = "편의시설 경도")
        String longitude;
        @Schema(description = "편의시설 이용 꿀팁")
        String tips;
        @Schema(description = "이미지 등록 후 반환된 URL", example = "https://s3...")
        String imageUrl;

    }

}
