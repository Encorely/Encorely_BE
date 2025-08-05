package spring.encorely.controller.notificationController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import spring.encorely.apiPayload.ApiResponse;
import spring.encorely.dto.notificationDto.NotificationResponseDTO;
import spring.encorely.service.notificationService.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PutMapping("/{notificationId}")
    @Operation(summary = "알림 읽음 처리")
    public ApiResponse<String> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ApiResponse.onSuccess("읽음 처리 되었습니다.");
    }

    @GetMapping
    @Operation(summary = "사용자 알림 목록 가져오기")
    public ApiResponse<List<NotificationResponseDTO.GetNotification>> getNotifications(@AuthenticationPrincipal UserDetails userDetails) {
        return ApiResponse.onSuccess(notificationService.getNotifications(Long.parseLong(userDetails.getUsername())));
    }

}
