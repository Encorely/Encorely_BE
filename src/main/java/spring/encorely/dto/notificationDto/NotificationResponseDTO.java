package spring.encorely.dto.notificationDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class NotificationResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetNotification {

        Long notificationId;
        Long senderId;
        Long reviewId;
        String message;
        boolean isRead;
        LocalDateTime createdAt;

    }

}
