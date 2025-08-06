package spring.encorely.controller.noticeController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.encorely.apiPayload.ApiResponse;
import spring.encorely.dto.noticeDto.NoticeResponseDTO;
import spring.encorely.service.noticeService.NoticeService;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping
    @Operation(summary = "공지 불러오기")
    public ApiResponse<List<NoticeResponseDTO.GetNotice>> getNotices() {
        return ApiResponse.onSuccess(noticeService.getNotices());
    }

}
