package spring.encorely.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationSettingType {
    SERVICE("서비스 이용 알림", "이용 내역, 서비스 진행사항 알림"),
    BENEFIT("쿠폰, 이벤트 등 혜택 알림", "광고성 정보 수신 동의에 의한 혜택 관련 알림");

    private final String title;
    private final String description;

}
