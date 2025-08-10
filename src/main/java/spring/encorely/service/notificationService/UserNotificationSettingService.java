package spring.encorely.service.notificationService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.encorely.apiPayload.code.status.ErrorStatus;
import spring.encorely.apiPayload.exception.handler.NotificationHandler;
import spring.encorely.domain.notification.UserNotificationSetting;
import spring.encorely.domain.user.User;
import spring.encorely.dto.userDto.UserResponseDTO;
import spring.encorely.repository.noticeRepository.UserNotificationSettingRepository;
import spring.encorely.service.userService.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserNotificationSettingService {

    private final UserNotificationSettingRepository userNotificationSettingRepository;
    private final UserService userService;

    public UserNotificationSetting findById(Long id) {
        return userNotificationSettingRepository.findById(id).orElseThrow(() -> new NotificationHandler(ErrorStatus.NOTIFICATION_SETTING_NOT_FOUND));
    }

    public List<UserResponseDTO.UserNotificationSetting> getNotificationSettings(Long userId) {
        User user = userService.findById(userId);

        List<UserResponseDTO.UserNotificationSetting> dtos = new ArrayList<>();
        List<UserNotificationSetting> settings = userNotificationSettingRepository.findAllByUser(user);

        for (UserNotificationSetting setting : settings) {
            UserResponseDTO.UserNotificationSetting dto = UserResponseDTO.UserNotificationSetting.builder()
                    .settingId(setting.getId())
                    .type(setting.getNotificationSettingType().name())
                    .title(setting.getNotificationSettingType().getTitle())
                    .description(setting.getNotificationSettingType().getDescription())
                    .build();

            dtos.add(dto);
        }

        return dtos;
    }

    public void toggleNotificationSetting(Long settingId) {
        UserNotificationSetting notificationSetting = findById(settingId);
        notificationSetting.setEnabled(!notificationSetting.isEnabled());
    }

}
