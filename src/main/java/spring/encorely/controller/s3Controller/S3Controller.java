package spring.encorely.controller.s3Controller;

import com.amazonaws.services.s3.AmazonS3;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import spring.encorely.apiPayload.ApiResponse;
import spring.encorely.dto.reviewDto.ReviewResponseDTO;
import spring.encorely.dto.s3Dto.S3ResponseDTO;
import spring.encorely.service.reviewService.ReviewImageService;
import spring.encorely.service.s3Service.S3Service;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/s3")
public class S3Controller {

    private final S3Service s3Service;
    private final ReviewImageService reviewImageService;

    @GetMapping("/presigned-url")
    @Operation(summary = "이미지 업로드를 위한 presigned-url GET")
    public ApiResponse<S3ResponseDTO> generatePresignedUrl(@RequestParam String fileName, @RequestParam String contentType) {
        return ApiResponse.onSuccess(s3Service.generatePresignedUrl(fileName, contentType));
    }

    @PostMapping("/upload-complete")
    @Operation(summary = "이미지 업로드 완료 알림")
    public ApiResponse<ReviewResponseDTO.CreateReviewImage> saveUploadedImage(@RequestParam String key) {
        return ApiResponse.onSuccess(reviewImageService.createReviewImage(key));
    }

}
