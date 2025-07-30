package spring.encorely.domain.reviewDetail;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FacilityCategory {
    PARKING_LOT("주차장"),
    RESTROOM("화장실");

    private final String description;
}
