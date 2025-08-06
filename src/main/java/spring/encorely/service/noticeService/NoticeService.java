package spring.encorely.service.noticeService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.encorely.domain.notification.Notification;
import spring.encorely.domain.notification.UserNotificationSetting;
import spring.encorely.domain.user.User;
import spring.encorely.dto.noticeDto.NoticeResponseDTO;
import spring.encorely.dto.userDto.UserResponseDTO;
import spring.encorely.notice.Notice;
import spring.encorely.repository.noticeRepository.NoticeRepository;
import spring.encorely.repository.noticeRepository.UserNotificationSettingRepository;
import spring.encorely.service.userService.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserService userService;
    private final UserNotificationSettingRepository userNotificationSettingRepository;

    public List<NoticeResponseDTO.GetNotice> getNotices() {
        List<NoticeResponseDTO.GetNotice> dtos = new ArrayList<>();
        List<Notice> notices = noticeRepository.findAllByOrderByCreatedAtDesc();

        for (Notice notice : notices) {
            NoticeResponseDTO.GetNotice dto = NoticeResponseDTO.GetNotice.builder()
                    .noticeId(notice.getId())
                    .noticeTitle(notice.getTitle())
                    .noticeContent(notice.getContent())
                    .createdAt(notice.getCreatedAt())
                    .build();

            dtos.add(dto);
        }

        return dtos;
    }

}
