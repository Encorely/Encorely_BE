package spring.encorely.dto.noticeDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class NoticeResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetNotice {
        Long noticeId;
        String noticeTitle;
        String noticeContent;
        LocalDateTime createdAt;
    }

}
