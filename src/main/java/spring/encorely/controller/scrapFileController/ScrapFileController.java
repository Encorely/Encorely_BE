package spring.encorely.controller.scrapFileController;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final ScrapFileService scrapFileService;

    @PostMapping("/{fileId}/review")
    public ApiResponse<ScrapFileResponseDTO.addReviewToFile> addReviewToFile(@PathVariable Long fileId,
                                                                             @RequestBody ScrapFileRequestDTO.addReviewToFile request) {
        return ApiResponse.onSuccess(fileService.addReviewToFile(fileId, request));
    }

    @PostMapping
    public ApiResponse<ScrapFileResponseDTO.addFile> addFile(@AuthenticationPrincipal UserDetails userDetails) {
        return ApiResponse.onSuccess(scrapFileService.addFile(Long.parseLong(userDetails.getUsername())));
    }

    @PatchMapping("/{fileId}")
    public ApiResponse<ScrapFileResponseDTO.updateFileName> updateFileName(@AuthenticationPrincipal UserDetails userDetails,
                                                                           @PathVariable Long fileId,
                                                                           @RequestBody ScrapFileRequestDTO.updateFileName request) {
        return ApiResponse.onSuccess(scrapFileService.updateFileName(Long.parseLong(userDetails.getUsername()), fileId, request));
    }
}

