package spring.encorely.dto.reviewDetailDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDetailRequestDto {

    @NotNull(message = "사용자 ID는 필수입니다.")
    private Long userId; // 리뷰 작성자 ID

    @NotNull(message = "홀 ID는 필수입니다.")
    private Long hallId;

    @NotBlank(message = "공연 이름은 필수입니다.")
    private String showName;

    @NotBlank(message = "아티스트 이름은 필수입니다.")
    private String artistName;

    @NotBlank(message = "좌석 구역은 필수입니다.")
    private String seatArea;

    @NotBlank(message = "좌석 열은 필수입니다.")
    private String seatRow;

    @NotBlank(message = "좌석 번호는 필수입니다.")
    private String seatNumber;

    @NotNull(message = "별점은 필수입니다.")
    private Float rating;

    @NotBlank(message = "리뷰 내용은 필수입니다.")
    private String comment; // 후기 자체에 대한 간단한 코멘트 필드

    @NotBlank(message = "상세 내용은 필수입니다.")
    private String detail; // 후기의 상세 내용 필드

    private String seatDetail; // 좌석 상세 설명

    @NotNull(message = "공연 날짜는 필수입니다.")
    private LocalDate showDate;

    @NotNull(message = "회차는 필수입니다.")
    private Integer round;
}
