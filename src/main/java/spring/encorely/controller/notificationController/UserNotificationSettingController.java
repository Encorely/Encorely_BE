package spring.encorely.controller.notificationController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import spring.encorely.apiPayload.ApiResponse;
import spring.encorely.dto.userDto.UserResponseDTO;
import spring.encorely.service.notificationService.UserNotificationSettingService;

import java.util.List;

@RestController
@RequestMapping("/api/notification-settings")
@RequiredArgsConstructor
public class UserNotificationSettingController {

    private final UserNotificationSettingService userNotificationSettingService;

    @GetMapping
    @Operation(summary = "알림설정 가져오기")
    public ApiResponse<List<UserResponseDTO.UserNotificationSetting>> getNotificationSettings(@AuthenticationPrincipal UserDetails userDetails) {
        return ApiResponse.onSuccess(userNotificationSettingService.getNotificationSettings(Long.parseLong(userDetails.getUsername())));
    }

    @PutMapping("/{settingId}")
    @Operation(summary = "알림 온오프")
    public ApiResponse<String> toggleNotificationSetting(@PathVariable Long settingId) {
        userNotificationSettingService.toggleNotificationSetting(settingId);
        return ApiResponse.onSuccess("알림 온오프가 수정되었습니다.");
    }

}
