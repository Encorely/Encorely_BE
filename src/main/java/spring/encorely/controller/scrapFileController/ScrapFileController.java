package spring.encorely.controller.scrapFileController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import spring.encorely.apiPayload.ApiResponse;
import spring.encorely.dto.scrapDto.ScrapFileRequestDTO;
import spring.encorely.dto.scrapDto.ScrapFileResponseDTO;
import spring.encorely.service.scrapService.ScrapFileService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class ScrapFileController {
    private final ScrapFileService fileService;

    @PostMapping("/{fileId}/review")
    public ApiResponse<ScrapFileResponseDTO.addReviewToFile> addReviewToFile(@PathVariable Long fileId,
                                                                             @RequestBody ScrapFileRequestDTO.addReviewToFile request) {
        return ApiResponse.onSuccess(fileService.addReviewToFile(fileId, request));
    }
}
