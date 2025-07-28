package spring.encorely.dto.reviewDetailDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDetailRequestDto {

    @NotNull(message = "공연장 ID는 필수입니다.")
    private Long hallId;

    @NotNull(message = "사용자 ID는 필수입니다.")
    private Long userId;

    @NotBlank(message = "공연명은 필수입니다.")
    private String showName;

    @NotBlank(message = "아티스트명은 필수입니다.")
    private String artistName;

    private String seatArea;
    private String seatRow;
    private String seatNumber;

    @NotNull(message = "평점은 필수입니다.")
    private Float rating;

    private String comment;
    private String detail;
    private String seatDetail;

    @NotNull(message = "공연 날짜는 필수입니다.")
    private LocalDate showDate;

    private Integer round;

    private List<String> imageUrls;
}