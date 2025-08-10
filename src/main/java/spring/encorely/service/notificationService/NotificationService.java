package spring.encorely.service.notificationService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.encorely.apiPayload.code.status.ErrorStatus;
import spring.encorely.apiPayload.exception.handler.NotificationHandler;
import spring.encorely.component.NotificationMessageFactory;
import spring.encorely.domain.enums.NotificationSettingType;
import spring.encorely.domain.enums.NotificationType;
import spring.encorely.domain.notification.Notification;
import spring.encorely.domain.notification.UserNotificationSetting;
import spring.encorely.domain.review.Review;
import spring.encorely.domain.user.User;
import spring.encorely.dto.notificationDto.NotificationResponseDTO;
import spring.encorely.dto.userDto.UserResponseDTO;
import spring.encorely.repository.noticeRepository.UserNotificationSettingRepository;
import spring.encorely.repository.notificationRepository.NotificationRepository;
import spring.encorely.service.reviewService.ReviewService;
import spring.encorely.service.userService.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMessageFactory notificationMessageFactory;
    private final UserService userService;
    private final UserNotificationSettingRepository userNotificationSettingRepository;

    public Notification findById(Long id) {
        return notificationRepository.findById(id).orElseThrow(() -> new NotificationHandler(ErrorStatus.NOTIFICATION_NOT_FOUND));
    }

    @Transactional
    public void createNotification(User sender, Review review, NotificationType type, String customMessage) {
        User receiver = review.getUser();

        if (receiver.getId().equals(sender.getId())) return;
        if (!receiver.allowsNotification(NotificationSettingType.SERVICE)) return;

        String message = notificationMessageFactory.createMessage(type, sender, review, customMessage);

        Notification notification = Notification.builder()
                .receiver(receiver)
                .sender(sender)
                .review(review)
                .notificationType(type)
                .message(message)
                .isRead(false)
                .build();

        notificationRepository.save(notification);
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = findById(notificationId);
        notification.setRead(true);
    }

    @Transactional
    public void createNotificationSettings(User user) {
        UserNotificationSetting service = UserNotificationSetting.builder()
                .notificationSettingType(NotificationSettingType.SERVICE)
                .user(user)
                .enabled(true)
                .build();

        UserNotificationSetting benefit = UserNotificationSetting.builder()
                .notificationSettingType(NotificationSettingType.BENEFIT)
                .user(user)
                .enabled(true)
                .build();

        userNotificationSettingRepository.save(service);
        userNotificationSettingRepository.save(benefit);
    }

    public List<NotificationResponseDTO.GetNotification> getNotifications(Long userId) {
        User user = userService.findById(userId);
        List<NotificationResponseDTO.GetNotification> dtos = new ArrayList<>();
        List<Notification> notifications = notificationRepository.findAllByReceiver(user);

        for (Notification notification : notifications) {
            NotificationResponseDTO.GetNotification dto = NotificationResponseDTO.GetNotification.builder()
                    .notificationId(notification.getId())
                    .senderId(notification.getSender().getId())
                    .reviewId(notification.getReview().getId())
                    .message(notification.getMessage())
                    .isRead(notification.isRead())
                    .createdAt(notification.getCreatedAt())
                    .build();

            dtos.add(dto);
        }

        return dtos;
    }

}
