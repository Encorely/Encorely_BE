package spring.encorely.domain.reviewDetail;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RestaurantCategory {
    // 맛집 카테고리
    RESTAURANT("밥집"),
    BAR("술집"),
    CAFE("카페"),
    ETC_FOOD("기타 맛집");

    private final String koreanName;
}