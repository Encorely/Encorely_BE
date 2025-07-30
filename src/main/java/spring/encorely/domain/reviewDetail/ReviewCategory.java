package spring.encorely.domain.reviewDetail;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReviewCategory {
    PERFORMANCE("공연 리뷰"),
    RESTAURANT("맛집 리뷰"),
    FACILITY("편의시설 리뷰");

    private final String description;
}
