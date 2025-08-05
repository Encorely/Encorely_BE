package spring.encorely.controller.scrapFileController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import spring.encorely.apiPayload.ApiResponse;
import spring.encorely.domain.enums.ReviewCategory;
import spring.encorely.dto.reviewDto.ReviewResponseDTO;
import spring.encorely.dto.scrapDto.ScrapFileRequestDTO;
import spring.encorely.dto.scrapDto.ScrapFileResponseDTO;
import spring.encorely.service.scrapService.ScrapFileService;
import spring.encorely.service.scrapService.ScrapReviewService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class ScrapFileController {
    private final ScrapFileService fileService;
    private final ScrapFileService scrapFileService;
    private final ScrapReviewService scrapReviewService;

    @PostMapping("/{fileId}/review")
    @Operation(summary = "스크랩 폴더에 리뷰 등록/스크랩 취소")
    public ApiResponse<ScrapFileResponseDTO.addReviewToFile> addReviewToFile(@PathVariable Long fileId,
                                                                             @RequestBody ScrapFileRequestDTO.addReviewToFile request) {
        return ApiResponse.onSuccess(fileService.toggleScrapReview(fileId, request));
    }

    @PostMapping
    @Operation(summary = "스크랩 폴더 추가")
    public ApiResponse<ScrapFileResponseDTO.addFile> addFile(@AuthenticationPrincipal UserDetails userDetails) {
        return ApiResponse.onSuccess(scrapFileService.addFile(Long.parseLong(userDetails.getUsername())));
    }

    @PatchMapping("/{fileId}")
    @Operation(summary = "스크랩 폴더 이름 변경")
    public ApiResponse<ScrapFileResponseDTO.updateFileName> updateFileName(@AuthenticationPrincipal UserDetails userDetails,
                                                                           @PathVariable Long fileId,
                                                                           @RequestBody ScrapFileRequestDTO.updateFileName request) {
        return ApiResponse.onSuccess(scrapFileService.updateFileName(Long.parseLong(userDetails.getUsername()), fileId, request));
    }

    @PutMapping("/move")
    @Operation(summary = "스크랩 리뷰 폴더 이동")
    public ApiResponse<String> moveScrapReview(@AuthenticationPrincipal UserDetails userDetails,
                                               ScrapFileRequestDTO.MoveReview request) {
        scrapFileService.moveScrapReviewToAnotherFolder(Long.parseLong(userDetails.getUsername()), request);
        return ApiResponse.onSuccess("폴더가 변경되었습니다.");
    }

    @DeleteMapping("/{fileId}")
    @Operation(summary = "폴더 삭제")
    public ApiResponse<String> deleteFile(@PathVariable Long fileId) {
        scrapFileService.deleteFile(fileId);
        return ApiResponse.onSuccess("폴더가 삭제되었습니다.");
    }

    @GetMapping
    @Operation(summary = "스크랩북 전체 폴더 불러오기")
    public ApiResponse<ScrapFileResponseDTO.GetFile> getFiles(@AuthenticationPrincipal UserDetails userDetails) {
        return ApiResponse.onSuccess(scrapFileService.getFiles(Long.parseLong(userDetails.getUsername())));
    }

    @GetMapping("/{fileId}")
    @Operation(summary = "폴더 내 리뷰 불러오기")
    public ApiResponse<ScrapFileResponseDTO.GetScrapReview> getScrapReview(@PathVariable Long fileId,
                                                                           @RequestParam(value = "hallId", required = false) Long hallId,
                                                                           @RequestParam(value = "categories", required = false) List<ReviewCategory> categories,
                                                                           @RequestParam(value = "sort", defaultValue = "latest") String sort) {
        return ApiResponse.onSuccess(scrapReviewService.getScrapReviews(fileId, hallId, categories, sort));
    }

}

